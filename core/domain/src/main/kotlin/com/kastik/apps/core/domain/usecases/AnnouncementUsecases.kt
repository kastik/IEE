package com.kastik.apps.core.domain.usecases

import androidx.paging.PagingData
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.result.Result
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetHomeAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<PagingData<Announcement>> {
        return combine(
            authenticationRepository.getIsSignedIn(),
            userPreferencesRepository.getSortType()
        ) { _, sortType ->
            sortType
        }.flatMapLatest { sortType ->
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
class GetFilteredAnnouncementsUseCase @Inject constructor(
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


//TODO When changing the subscribed tags the storedIds will not match anything,
// leading to missed announcements for the first launch of this UseCase
class CheckNewAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val profileRepository: ProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {

    private companion object {
        const val PAGE_SIZE = 20
        const val MAX_PAGES_SAFETY_LIMIT = 2
        const val MAX_HISTORY_SIZE = 50
    }

    suspend operator fun invoke(): Result<List<Announcement>, GeneralRefreshError> {

        val subscribedTagIds =
            profileRepository.getEmailSubscriptions().first().map { it.id }

        if (subscribedTagIds.isEmpty()) return Result.Success(emptyList<Announcement>())

        val notifiedAnnouncementIds =
            userPreferencesRepository.getNotifiedAnnouncementIds().first().toMutableSet()

        val announcementsToNotify = mutableListOf<Announcement>()

        val coldStart = notifiedAnnouncementIds.isEmpty()

        var page = 1

        while (page <= MAX_PAGES_SAFETY_LIMIT) {

            val announcementResult = announcementRepository.fetchAnnouncements(
                page = page,
                perPage = PAGE_SIZE,
                sortType = SortType.DESC,
                tagIds = subscribedTagIds
            )

            when (announcementResult) {
                is Result.Success -> {
                    val notNotifiedAnnouncements =
                        announcementResult.data.filter { !notifiedAnnouncementIds.contains(it.id) }

                    if (notNotifiedAnnouncements.isNotEmpty()) {
                        announcementsToNotify.addAll(notNotifiedAnnouncements)
                    }

                    page++
                }

                is Result.Error -> {
                    return announcementResult
                }

            }
        }

        if (coldStart) {
            val updatedHistory = announcementsToNotify.toList().takeLast(MAX_HISTORY_SIZE)
            userPreferencesRepository.setNotifiedAnnouncementId(updatedHistory.map { it.id })
            return Result.Success(emptyList())
        } else {
            return Result.Success(announcementsToNotify)
        }
    }
}