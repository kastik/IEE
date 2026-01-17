package com.kastik.apps.core.data.repository

import android.accounts.AuthenticatorException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.withTransaction
import com.kastik.apps.core.data.mappers.toAnnouncement
import com.kastik.apps.core.data.mappers.toAnnouncementEntity
import com.kastik.apps.core.data.mappers.toAttachmentEntity
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.data.mappers.toBodyEntity
import com.kastik.apps.core.data.mappers.toTagCrossRefs
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediator
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AnnouncementRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
) : AnnouncementRepository {
    private val announcementLocalDataSource = database.announcementDao()
    private val tagsLocalDataSource = database.tagsDao()
    private val authorLocalDataSource = database.authorDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedAnnouncements(
        sortType: SortType,
        query: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ) = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 60,
            prefetchDistance = 10,
            enablePlaceholders = true
        ), remoteMediator = AnnouncementRemoteMediator(
            sortType = sortType,
            query = query,
            authorIds = authorIds,
            tagIds = tagIds,
            database = database,
            announcementRemoteDataSource = announcementRemoteDataSource,
        ), pagingSourceFactory = {
            announcementLocalDataSource.getPagedAnnouncements(
                sortType = sortType,
                query = query,
                tagIds = tagIds,
                authorIds = authorIds,
            )
        }).flow.map { pagingData ->
        pagingData.map { it.toAnnouncement() }
    }

    override fun getAnnouncementsQuickResults(
        sortType: SortType,
        query: String
    ): Flow<List<Announcement>> {
        return announcementLocalDataSource.getQuickSearchAnnouncements(
            sortType = sortType,
            query = query,
        ).map { entities ->
            entities.map { it.toAnnouncement() }
        }
    }

    override suspend fun refreshAnnouncementWithId(id: Int) = withContext(Dispatchers.IO) {
        val remote = try {
            announcementRemoteDataSource.fetchAnnouncementWithId(id).data
        } catch (e: HttpException) {
            if (e.code() == 401) {
                throw AuthenticatorException()
            } else {
                throw e
            }
        }

        database.withTransaction {
            authorLocalDataSource.insertOrIgnoreAuthors(listOf(remote.author.toAuthorEntity()))
            tagsLocalDataSource.insertOrIgnoreTags(remote.tags.map { it.toTagEntity() })

            announcementLocalDataSource.insertOrReplaceAnnouncement(remote.toAnnouncementEntity())

            announcementLocalDataSource.insertOrReplaceTagCrossRefs(remote.toTagCrossRefs())
            announcementLocalDataSource.insertOrReplaceAnnouncementBody(remote.toBodyEntity())
            announcementLocalDataSource.insertOrReplaceAnnouncementAttachments(remote.attachments.map { it.toAttachmentEntity() })
        }
    }

    override fun getAnnouncementWithId(id: Int): Flow<Announcement?> {
        return announcementLocalDataSource.getAnnouncementWithId(id)
            .map { it?.toAnnouncement() }
    }

    override suspend fun getAttachmentUrl(attachmentId: Int): String {
        return announcementLocalDataSource.getAttachmentWithId(attachmentId)
    }

    override suspend fun clearAnnouncementCache() = withContext(Dispatchers.IO) {
        announcementLocalDataSource.clearAllAnnouncements()
        announcementLocalDataSource.clearBodies()
        announcementLocalDataSource.clearAttachments()
        announcementLocalDataSource.clearTagCrossRefs()
    }
}