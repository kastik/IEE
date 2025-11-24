package com.kastik.apps.core.domain.usecases

import androidx.paging.PagingData
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementTag
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.model.aboard.Author
import kotlinx.coroutines.flow.Flow

class GetPagedAnnouncementsUseCase(
    private val repo: AnnouncementRepository
) {
    operator fun invoke(): Flow<PagingData<AnnouncementPreview>> =
        repo.getPagedAnnouncements()
}

class GetPagedFilteredAnnouncementsUseCase(
    private val repo: AnnouncementRepository
) {
    operator fun invoke(
        query: String?,
        authorIds: List<Int>?,
        tagIds: List<Int>?
    ): Flow<PagingData<AnnouncementPreview>> =
        repo.getPagedFilteredAnnouncements(query, authorIds, tagIds)
}

class GetAnnouncementWithIdUseCase(
    private val repo: AnnouncementRepository
) {
    operator fun invoke(id: Int): Flow<AnnouncementView> =
        repo.getAnnouncementWithId(id)
}

class GetTagsUseCase(
    private val repo: AnnouncementRepository
) {
    suspend operator fun invoke(): Flow<List<AnnouncementTag>> =
        repo.getTags()
}


class GetAuthorsUseCase(
    private val repo: AnnouncementRepository
) {
    suspend operator fun invoke(): Flow<List<Author>> =
        repo.getAuthors()
}