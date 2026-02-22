package com.kastik.apps.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.withTransaction
import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.extractImages
import com.kastik.apps.core.data.mappers.toAnnouncement
import com.kastik.apps.core.data.mappers.toAnnouncementEntity
import com.kastik.apps.core.data.mappers.toAttachmentEntity
import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.data.mappers.toBodyEntity
import com.kastik.apps.core.data.mappers.toPrivateRefreshError
import com.kastik.apps.core.data.mappers.toPublicRefreshError
import com.kastik.apps.core.data.mappers.toTagCrossRefs
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediator
import com.kastik.apps.core.data.utils.Base64ImageExtractor
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Instant

@Singleton
internal class AnnouncementRepositoryImpl @Inject constructor(
    private val crashlytics: Crashlytics,
    private val database: AppDatabase,
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val base64ImageExtractor: Base64ImageExtractor,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AnnouncementRepository {
    private val announcementLocalDataSource = database.announcementDao()
    private val tagsLocalDataSource = database.tagsDao()
    private val authorLocalDataSource = database.authorDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedAnnouncements(
        sortType: SortType,
        titleQuery: String,
        bodyQuery: String,
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
            titleQuery = titleQuery,
            bodyQuery = bodyQuery,
            authorIds = authorIds,
            tagIds = tagIds,
            crashlytics = crashlytics,
            database = database,
            announcementRemoteDataSource = announcementRemoteDataSource,
            base64ImageExtractor = base64ImageExtractor
        ), pagingSourceFactory = {
            announcementLocalDataSource.getPagedAnnouncements(
                sortType = sortType,
                titleQuery = titleQuery,
                bodyQuery = bodyQuery,
                tagIds = tagIds,
                authorIds = authorIds,
            )
        }).flow.map { pagingData ->
        pagingData.map { it.toAnnouncement() }
    }

    override suspend fun fetchAnnouncements(
        page: Int,
        perPage: Int,
        sortType: SortType,
        titleQuery: String,
        bodyQuery: String,
        authorIds: List<Int>,
        tagIds: List<Int>,
        updatedAfter: Instant?
    ): Result<List<Announcement>, GeneralRefreshError> =
        try {

            val greekLocalTime: LocalDateTime? =
                updatedAfter?.toLocalDateTime(TimeZone.of("Europe/Athens"))

            Result.Success(
                announcementRemoteDataSource.fetchPagedAnnouncements(
                    page = page,
                    perPage = perPage,
                    sortBy = sortType,
                    title = titleQuery,
                    body = bodyQuery,
                    tagId = tagIds,
                    authorId = authorIds,
                    updatedAfter = greekLocalTime
                ).data.map { it.toAnnouncement() }
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.Error(e.toPublicRefreshError())
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

    override suspend fun refreshAnnouncementWithId(id: Int) = withContext(ioDispatcher) {
        try {
            val remote = announcementRemoteDataSource.fetchAnnouncementWithId(id).data

            database.withTransaction {
                authorLocalDataSource.upsertAuthors(remote.author.toAuthorEntity())
                tagsLocalDataSource.upsertTags(remote.tags.map { it.toTagEntity() })

                announcementLocalDataSource.upsertAnnouncements(remote.toAnnouncementEntity())

                announcementLocalDataSource.upsertTagCrossRefs(remote.toTagCrossRefs())
                announcementLocalDataSource.upsertBodies(
                    remote.extractImages(base64ImageExtractor).toBodyEntity()
                )
                announcementLocalDataSource.upsertAttachments(remote.attachments.map { it.toAttachmentEntity() })
            }
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toPrivateRefreshError())
        }
    }

    override fun getAnnouncementWithId(id: Int): Flow<Announcement?> {
        return announcementLocalDataSource.getAnnouncementWithId(id)
            .map { it?.toAnnouncement() }
    }

    override suspend fun getAttachmentUrl(attachmentId: Int): String = withContext(ioDispatcher) {
        announcementLocalDataSource.getAttachmentWithId(attachmentId)
    }

    override suspend fun clearAnnouncementCache() = withContext(ioDispatcher) {
        try {
            announcementLocalDataSource.clearAllAnnouncements()
            announcementLocalDataSource.clearBodies()
            announcementLocalDataSource.clearAttachments()
            announcementLocalDataSource.clearTagCrossRefs()
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(StorageError)
        }
    }
}