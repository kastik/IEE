package com.kastik.apps.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.data.mappers.toRoomEntities
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.database.dao.RemoteKeysDao
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import com.kastik.apps.core.domain.repository.SortType
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import java.net.UnknownHostException

@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediator(
    private val query: String,
    private val tagIds: List<Int>,
    private val authorIds: List<Int>,
    private val sortType: SortType,
    private val database: AppDatabase,
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
) : RemoteMediator<Int, AnnouncementWithoutBody>() {
    private val remoteKeysDao: RemoteKeysDao = database.remoteKeysDao()
    private val authorsLocalDataSource: AuthorsDao = database.authorDao()
    private val announcementTagsLocalDataSource: TagsDao = database.tagsDao()
    private val announcementLocalDataSource: AnnouncementDao = database.announcementDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AnnouncementWithoutBody>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey

            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
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
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.insertKeys(keys)
                response.data.forEach { dto ->
                    announcementTagsLocalDataSource.insertTags(
                        dto.tags.map { it.toTagEntity() }
                    )
                    authorsLocalDataSource.insertAuthor(
                        dto.author.toAuthorEntity()
                    )
                    announcementLocalDataSource.addAnnouncement(
                        dto.toRoomEntities()
                    )
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: UnknownHostException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, AnnouncementWithoutBody>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { announcement ->
                remoteKeysDao.getKeyByAnnouncementId(announcement.announcement.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, AnnouncementWithoutBody>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { announcement ->
                remoteKeysDao.getKeyByAnnouncementId(announcement.announcement.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, AnnouncementWithoutBody>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.announcement?.id?.let { repoId ->
                remoteKeysDao.getKeyByAnnouncementId(repoId)
            }
        }
    }
}