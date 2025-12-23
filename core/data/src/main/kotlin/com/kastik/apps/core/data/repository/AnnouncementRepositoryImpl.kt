package com.kastik.apps.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.kastik.apps.core.data.mappers.toAnnouncementPreview
import com.kastik.apps.core.data.mappers.toAnnouncementView
import com.kastik.apps.core.data.mappers.toRoomEntities
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediator
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.SortType
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class AnnouncementRepositoryImpl(
    private val database: AppDatabase,
    private val announcementLocalDataSource: AnnouncementDao,
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
) : AnnouncementRepository {

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
        ),
        remoteMediator = AnnouncementRemoteMediator(
            database = database,
            announcementRemoteDataSource = announcementRemoteDataSource,
            sortType = sortType,
            query = query,
            authorIds = authorIds,
            tagIds = tagIds
        ),
        pagingSourceFactory = {
            announcementLocalDataSource.getPagedAnnouncements(
                query = query,
                tagIds = tagIds,
                authorIds = authorIds,
                sortType = sortType.name
            )
        }
    ).flow.map { pagingData ->
        pagingData.map { it.toAnnouncementPreview() }
    }

    override fun getAnnouncementsQuickResults(
        sortType: SortType,
        query: String
    ): Flow<List<AnnouncementPreview>> {
        //TODO TRIGGER AN API REQUEST
        return announcementLocalDataSource.getQuickSearchAnnouncements(
            query = query,
            sortType = sortType.name
        ).map { entities ->
            entities.map { it.toAnnouncementPreview() }
        }
    }

    override suspend fun refreshAnnouncementWithId(id: Int) {
        val remote = announcementRemoteDataSource.fetchAnnouncementWithId(id).data
        announcementLocalDataSource.updateAnnouncement(remote.toRoomEntities())
    }

    override fun getAnnouncementWithId(id: Int): Flow<AnnouncementView> {
        return announcementLocalDataSource.getAnnouncementWithId(id)
            .filterNotNull()
            .map { it.toAnnouncementView() }
    }

    override suspend fun getAttachmentUrl(attachmentId: Int): String {
        return announcementLocalDataSource.getAttachmentWithId(attachmentId)
    }

    override suspend fun clearAnnouncementCache() {
        announcementLocalDataSource.clearAnnouncements()

    }
}