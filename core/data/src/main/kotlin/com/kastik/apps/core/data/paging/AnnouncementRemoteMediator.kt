package com.kastik.apps.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kastik.apps.core.data.mappers.toAnnouncementEntity
import com.kastik.apps.core.data.mappers.toAttachmentEntity
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.data.mappers.toBodyEntity
import com.kastik.apps.core.data.mappers.toTagCrossRefs
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import com.kastik.apps.core.network.model.aboard.AnnouncementPageResponse
import java.net.UnknownHostException

@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediator(
    private val query: String = "",
    private val tagIds: List<Int> = emptyList(),
    private val authorIds: List<Int> = emptyList(),
    private val sortType: SortType = SortType.DESC,
    private val database: AppDatabase,
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
) : RemoteMediator<Int, AnnouncementPreviewRelation>() {
    private val remoteKeysDao = database.remoteKeysDao()
    private val announcementLocalDataSource = database.announcementDao()
    private val authorLocalDataSource = database.authorDao()
    private val tagsLocalDataSource = database.tagsDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AnnouncementPreviewRelation>
    ): MediatorResult {
        val page = when (loadType) {

            LoadType.REFRESH -> 1

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    //Note, here we check "remoteKeys", cause if it's null it means no pages are loaded yet
                    //Should switch it to true once we find the issue in :feature:search
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        try {
            val response = announcementRemoteDataSource.fetchPagedAnnouncements(
                page = page,
                title = query,
                body = query,
                tagId = tagIds,
                authorId = authorIds,
                perPage = state.config.pageSize,
                sortBy = sortType
            )

            val endOfPaginationReached = response.data.isEmpty() || page >= response.meta.lastPage

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearKeys(
                        sortType = sortType,
                        query = query,
                        authorIds = authorIds,
                        tagIds = tagIds,
                    )
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val keys = response.data.map { dto ->
                    RemoteKeys(
                        announcementId = dto.id,
                        searchQuery = query,
                        authorIds = authorIds,
                        tagIds = tagIds,
                        sortType = sortType,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.insertOrReplaceKeys(keys)
                insertAnnouncement(response)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: UnknownHostException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun insertAnnouncement(dto: AnnouncementPageResponse) {

        val mappedAuthors = dto.data.map { it.author.toAuthorEntity() }
        authorLocalDataSource.insertOrIgnoreAuthors(mappedAuthors)

        val mappedAnnouncements = dto.data.map { it.toAnnouncementEntity() }
        announcementLocalDataSource.insertOrIgnoreAnnouncements(mappedAnnouncements)

        val mappedBodies = dto.data.map { it.toBodyEntity() }
        announcementLocalDataSource.insertOrIgnoreAnnouncementBody(mappedBodies)

        val mappedAttachments = dto.data.flatMap { it.attachments.map { it.toAttachmentEntity() } }
        announcementLocalDataSource.insertOrIgnoreAnnouncementAttachments(mappedAttachments)

        val mappedTags = dto.data.flatMap { it.tags.map { it.toTagEntity() } }
        tagsLocalDataSource.insertOrIgnoreTags(mappedTags)

        val mappedTagCrossRefs = dto.data.flatMap { it.toTagCrossRefs() }
        announcementLocalDataSource.insertOrIgnoreTagCrossRefs(mappedTagCrossRefs)

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, AnnouncementPreviewRelation>): RemoteKeys? {
        val lastItem = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?: return null

        return remoteKeysDao.getKeyByAnnouncementId(
            lastItem.announcement.id, sortType, query, authorIds, tagIds
        )
    }
}