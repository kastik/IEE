package com.kastik.apps.core.domain.usecases

import androidx.paging.PagingData
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.aboard.Announcement
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
                    sortType = sortType,
                    titleQuery = "",
                    bodyQuery = "",
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
        query: String = "",
        authorIds: List<Int> = emptyList(),
        tagIds: List<Int> = emptyList()
    ): Flow<PagingData<Announcement>> =
        combine(
            userPreferencesRepository.getSortType(),
            userPreferencesRepository.getSearchScope()
        ) { sortType, searchScope ->
            sortType to searchScope
        }.flatMapLatest { (sortType, searchScope) ->
            announcementRepository.getPagedAnnouncements(
                sortType = sortType,
                titleQuery = query.takeIf { searchScope.includesTitle } ?: "",
                bodyQuery = query.takeIf { searchScope.includesBody } ?: "",
                authorIds = authorIds,
                tagIds = tagIds
            )
        }
}

@ViewModelScoped
class GetForYouAnnouncementsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<PagingData<Announcement>> =
        combine(
            userPreferencesRepository.getSortType(),
            profileRepository.getEmailSubscriptions(),
            ::Pair
        ).flatMapLatest { (sortType, subscribedTags) ->
            announcementRepository.getPagedAnnouncements(
                sortType = sortType,
                tagIds = subscribedTags.map { it.id },
            )
        }
}

class GetSearchQuickResultsAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(query: String? = null) =
        userPreferencesRepository.getSortType()
            .flatMapLatest { sortType ->
                if (query.isNullOrBlank()) {
                    flowOf(persistentListOf())
                } else {
                    announcementRepository.getAnnouncementsQuickResults(sortType, query).map {
                        it.toImmutableList()
                    }
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