package com.kastik.apps.core.domain.usecases

import androidx.paging.PagingData
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.aboard.Announcement
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPagedAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<PagingData<Announcement>> {
        return userPreferencesRepository.getSortType()
            .flatMapLatest { sortType ->
                announcementRepository.getPagedAnnouncements(
                    sortType,
                    query = "",
                    authorIds = emptyList(),
                    tagIds = emptyList(),
                )
            }
    }
}

@ViewModelScoped
class GetPagedFilteredAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(
        query: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ): Flow<PagingData<Announcement>> =
        userPreferencesRepository.getSortType()
            .flatMapLatest { sortType ->
                announcementRepository.getPagedAnnouncements(sortType, query, authorIds, tagIds)
            }
}

class GetSearchQuickResultsAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(query: String? = null): Flow<List<Announcement>> =
        userPreferencesRepository.getSortType()
            .flatMapLatest { sortType ->
                if (query.isNullOrBlank()) {
                    flowOf(emptyList())
                } else {
                    announcementRepository.getAnnouncementsQuickResults(sortType, query)
                }
            }
}

class GetAnnouncementWithIdUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    operator fun invoke(id: Int): Flow<Announcement?> =
        announcementRepository.getAnnouncementWithId(id)
}


class RefreshAnnouncementWithIdUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(id: Int) =
        announcementRepository.refreshAnnouncementWithId(id)
}