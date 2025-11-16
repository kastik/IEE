package com.kastik.apps.core.domain.repository

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    //TODO This breaks clean architecture find a way to abstract PagingData without a performance penalty
    fun getPagedAnnouncements(): Flow<PagingData<AnnouncementPreview>>

    suspend fun getAnnouncementWithId(id: Int): AnnouncementView
}