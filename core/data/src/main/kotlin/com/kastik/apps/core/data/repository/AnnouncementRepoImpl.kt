package com.kastik.apps.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kastik.apps.core.data.mappers.toDomain
import com.kastik.apps.core.data.mediator.AnnouncementRemoteMediator
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnnouncementRepoImpl(
    private val remoteDataSource: AnnouncementRemoteDataSource,
    private val db: AppDatabase
) : AnnouncementRepository {

    @OptIn(ExperimentalPagingApi::class)
    //TODO This breaks clean architecture
    override fun getPagedAnnouncements(): Flow<PagingData<AnnouncementPreview>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 60,
                prefetchDistance = 10,
                enablePlaceholders = true
            ),
            remoteMediator = AnnouncementRemoteMediator(remote = remoteDataSource, db = db),
            pagingSourceFactory = { db.announcementDao().getPagingAnnouncementPreviews() }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toDomain()
            }
        }

    override suspend fun getAnnouncementWithId(id: Int): AnnouncementView {
        return db.announcementDao().getAnnouncementWithId(id).toDomain()
    }
}