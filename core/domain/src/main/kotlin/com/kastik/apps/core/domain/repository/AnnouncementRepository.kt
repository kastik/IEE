package com.kastik.apps.core.domain.repository

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface AnnouncementRepository {
    fun getPagedAnnouncements(
        sortType: SortType = SortType.Priority,
        titleQuery: String = "",
        bodyQuery: String = "",
        authorIds: List<Int> = emptyList(),
        tagIds: List<Int> = emptyList(),
    ): Flow<PagingData<Announcement>>

    suspend fun fetchAnnouncements(
        page: Int,
        perPage: Int,
        sortType: SortType = SortType.Priority,
        titleQuery: String = "",
        bodyQuery: String = "",
        authorIds: List<Int> = emptyList(),
        tagIds: List<Int> = emptyList(),
        updatedAfter: Instant? = null
    ): Result<List<Announcement>, GeneralRefreshError>

    fun getAnnouncementsQuickResults(
        sortType: SortType,
        query: String
    ): Flow<List<Announcement>>

    fun getAnnouncementWithId(id: Int): Flow<Announcement?>
    suspend fun getAttachmentUrl(attachmentId: Int): String
    suspend fun refreshAnnouncementWithId(id: Int): Result<Unit, AuthenticatedRefreshError>
    suspend fun clearAnnouncementCache(): Result<Unit, StorageError>
}