package com.kastik.apps.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kastik.apps.core.data.mappers.toDomain
import com.kastik.apps.core.data.mappers.toRoomEntities
import com.kastik.apps.core.data.mediator.AnnouncementRemoteMediator
import com.kastik.apps.core.data.pagingsource.AnnouncementPagingSource
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementTag
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class AnnouncementRepoImpl(
    private val remoteDataSource: AnnouncementRemoteDataSource,
    private val db: AppDatabase
) : AnnouncementRepository {

    @OptIn(ExperimentalPagingApi::class)
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

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedFilteredAnnouncements(
        query: String?,
        authorIds: List<Int>?,
        tagIds: List<Int>?
    ): Flow<PagingData<AnnouncementPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 40,
                prefetchDistance = 5,
                enablePlaceholders = true
            ),
            initialKey = 1,
            pagingSourceFactory = {
                AnnouncementPagingSource(
                    remote = remoteDataSource,
                    query = query,
                    tagIds = tagIds,
                    authorIds = authorIds
                )
            }
        ).flow
    }

    override fun getAnnouncementWithId(id: Int): Flow<AnnouncementView> {
        return flow {
            try {
                val remote = remoteDataSource.fetchAnnouncement(id).data
                emit(remote.toDomain())
                db.announcementDao().addAnnouncement(remote.toRoomEntities())
            } catch (e: Exception) {
                val local = db.announcementDao().getAnnouncementWithId(id)
                if (local != null) {
                    emit(local.toDomain())
                } else {
                    throw e
                }
            }
        }
    }


    override suspend fun getTags(): Flow<List<AnnouncementTag>> {
        return flowOf(remoteDataSource.fetchTags().data.map { it.toDomain() })
    }

    override suspend fun getAuthors(): Flow<List<Author>> {
        return flowOf(remoteDataSource.fetchAuthors().map { it.toDomain() })
    }

}