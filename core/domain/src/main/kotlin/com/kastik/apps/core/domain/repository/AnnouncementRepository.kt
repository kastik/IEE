package com.kastik.apps.core.domain.repository

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementTag
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.model.aboard.Author
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    //TODO This breaks clean architecture find a way to abstract PagingData without a performance penalty
    fun getPagedAnnouncements(): Flow<PagingData<AnnouncementPreview>>

    fun getPagedFilteredAnnouncements(
        query: String?,
        authorIds: List<Int>?,
        tagIds: List<Int>?
    ): Flow<PagingData<AnnouncementPreview>>

    fun getAnnouncementWithId(id: Int): Flow<AnnouncementView>

    suspend fun getTags(): Flow<List<AnnouncementTag>>

    suspend fun getAuthors(): Flow<List<Author>>

}