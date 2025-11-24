package com.kastik.apps.core.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kastik.apps.core.data.mappers.toRoomEntities
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import java.net.UnknownHostException

@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediator(
    private val remote: AnnouncementRemoteDataSource,
    private val db: AppDatabase
) : RemoteMediator<Int, AnnouncementWithoutBody>() {

    private val announcementDao = db.announcementDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    private var currentPage = 1
    private var lastPage = Int.MAX_VALUE

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, AnnouncementWithoutBody>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
                if (currentPage >= lastPage) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                currentPage + 1
            }
        }

        try {
            val response = remote.fetchAnnouncements(
                page = page,
                perPage = state.config.pageSize,
                title = null,
                body = null,
                authorId = null,
                tagId = null,
            )
            val newCurrentPage = response.meta.currentPage
            val newLastPage = response.meta.lastPage


            if (loadType == LoadType.REFRESH) {
                announcementDao.clearAnnouncements()
            }
            response.data.forEach { dto ->
                announcementDao.addAnnouncement(
                    dto.toRoomEntities()
                )
            }
            currentPage = newCurrentPage
            lastPage = newLastPage
            return MediatorResult.Success(endOfPaginationReached = currentPage >= lastPage)

        } catch (e: UnknownHostException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
