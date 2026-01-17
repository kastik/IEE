package com.kastik.apps.core.domain.repository

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.SortType
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun getPagedAnnouncements(
        sortType: SortType,
        query: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ): Flow<PagingData<Announcement>>

    fun getAnnouncementsQuickResults(
        sortType: SortType,
        query: String
    ): Flow<List<Announcement>>

    fun getAnnouncementWithId(id: Int): Flow<Announcement?>
    suspend fun getAttachmentUrl(attachmentId: Int): String
    suspend fun refreshAnnouncementWithId(id: Int)
    suspend fun clearAnnouncementCache()
}