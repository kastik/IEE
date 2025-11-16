package com.kastik.apps.core.domain.usecases

import androidx.paging.PagingData
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import kotlinx.coroutines.flow.Flow

class GetPagedAnnouncementsUseCase(
    private val repo: AnnouncementRepository
) {
    operator fun invoke(): Flow<PagingData<AnnouncementPreview>> =
        repo.getPagedAnnouncements()
}

class GetAnnouncementWithIdUseCase(
    private val repo: AnnouncementRepository
) {
    suspend operator fun invoke(id: Int): AnnouncementView =
        repo.getAnnouncementWithId(id)
}