package com.kastik.apps.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kastik.apps.core.data.mappers.toAnnouncementPreview
import com.kastik.apps.core.data.mappers.toAnnouncementView
import com.kastik.apps.core.data.mappers.toRoomEntities
import com.kastik.apps.core.data.paging.AnnouncementRemoteMediator
import com.kastik.apps.core.data.paging.FilteredAnnouncementPagingSource
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class AnnouncementRepositoryImpl(
    private val announcementLocalDataSource: AnnouncementDao,
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
) : AnnouncementRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedAnnouncements(): Flow<PagingData<AnnouncementPreview>> = Pager(
        config = PagingConfig(
            pageSize = 20, initialLoadSize = 60, prefetchDistance = 10, enablePlaceholders = true
        ),
        remoteMediator = AnnouncementRemoteMediator(
            announcementRemoteDataSource = announcementRemoteDataSource,
            announcementLocalDataSource = announcementLocalDataSource
        ),
        pagingSourceFactory = { announcementLocalDataSource.getPagingAnnouncementPreviews() }).flow.map { pagingData ->
        pagingData.map {
            it.toAnnouncementPreview()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedFilteredAnnouncements(
        query: String?, authorIds: List<Int>?, tagIds: List<Int>?
    ): Flow<PagingData<AnnouncementPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, initialLoadSize = 40, prefetchDistance = 5, enablePlaceholders = true
            ), initialKey = 1, pagingSourceFactory = {
                FilteredAnnouncementPagingSource(
                    announcementRemoteDataSource = announcementRemoteDataSource,
                    query = query,
                    tagIds = tagIds,
                    authorIds = authorIds
                )
            }).flow
    }

    override fun getAnnouncementWithId(id: Int): Flow<AnnouncementView> {
        //TODO Do this offline first
        return flow {
            try {
                val remote = announcementRemoteDataSource.fetchAnnouncementWithId(id).data
                emit(remote.toAnnouncementView())
                announcementLocalDataSource.addAnnouncement(remote.toRoomEntities())
            } catch (e: Exception) {
                val local = announcementLocalDataSource.getAnnouncementWithId(id)
                if (local != null) {
                    emit(local.toAnnouncementView())
                } else {
                    throw e
                }
            }
        }
    }

    override suspend fun clearAnnouncementCache() {
        announcementLocalDataSource.clearAnnouncements()

    }
}