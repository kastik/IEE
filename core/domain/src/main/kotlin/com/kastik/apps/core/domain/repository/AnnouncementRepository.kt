package com.kastik.apps.core.domain.repository

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun getPagedAnnouncements(): Flow<PagingData<AnnouncementPreview>>
    fun getPagedFilteredAnnouncements(
        query: String?,
        authorIds: List<Int>?,
        tagIds: List<Int>?
    ): Flow<PagingData<AnnouncementPreview>>
    fun getAnnouncementWithId(id: Int): Flow<AnnouncementView>
    suspend fun clearAnnouncementCache()
}