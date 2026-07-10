package com.kastik.apps.core.domain.usecases

import androidx.paging.PagingData
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Clock

class GetHomeAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    private data class FilterParams(
        val isSignedIn: Boolean,
        val sortType: SortType,
    )

    operator fun invoke(): Flow<PagingData<Announcement>> {
        return combine(
            authenticationRepository.isSignedIn,
            userPreferencesRepository.userPreferences
        ) { isSignedIn, userPreferences ->
            FilterParams(isSignedIn, userPreferences.sortType)
        }.distinctUntilChanged().flatMapLatest { params ->
            announcementRepository.getPagedAnnouncements(
                sortType = params.sortType,
                titleQuery = "",
                bodyQuery = "",
                authorIds = emptyList(),
                tagIds = emptyList(),
            )
        }
    }
}

class GetForYouAnnouncementsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val isForYouAvailableUseCase: IsForYouAvailableUseCase,
    private val isForYouEnabledUseCase: IsForYouEnabledUseCase,
) {
    private data class FilterParams(
        val sortType: SortType,
        val subscribedTagIds: List<Int>,
        val isForYouEnabled: Boolean,
        val isForYouAvailable: Boolean,
    )

    operator fun invoke(): Flow<PagingData<Announcement>> = combine(
        tagsRepository.subscribedTags,
        userPreferencesRepository.userPreferences,
        isForYouEnabledUseCase(),
        isForYouAvailableUseCase(),
    ) { subscribedTags, userPreferences, isForYouEnabled, isForYouAvailable ->
        FilterParams(
            sortType = userPreferences.sortType,
            subscribedTagIds = subscribedTags.map { it.id },
            isForYouEnabled = isForYouEnabled,
            isForYouAvailable = isForYouAvailable,
        )
    }.distinctUntilChanged().flatMapLatest { params ->
        if (!params.isForYouEnabled || !params.isForYouAvailable) {
            flowOf(PagingData.empty())
        } else {
            announcementRepository.getPagedAnnouncements(
                sortType = params.sortType,
                tagIds = params.subscribedTagIds,
            )
        }
    }
}

class GetFilteredAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    private data class FilterParams(
        val sortType: SortType,
        val titleQuery: String,
        val bodyQuery: String,
        val authorIds: List<Int>,
        val tagIds: List<Int>
    )

    operator fun invoke(
        query: String = "", authorIds: List<Int> = emptyList(), tagIds: List<Int> = emptyList()
    ): Flow<PagingData<Announcement>> =
        userPreferencesRepository.userPreferences.map { userPreferences ->
            FilterParams(
                sortType = userPreferences.sortType,
                titleQuery = query.takeIf { userPreferences.searchScope.includesTitle } ?: "",
                bodyQuery = query.takeIf { userPreferences.searchScope.includesBody } ?: "",
                authorIds = authorIds,
                tagIds = tagIds
            )
        }.distinctUntilChanged().flatMapLatest { params ->
            announcementRepository.getPagedAnnouncements(
                sortType = params.sortType,
                titleQuery = params.titleQuery,
                bodyQuery = params.bodyQuery,
                authorIds = params.authorIds,
                tagIds = params.tagIds,
            )
        }
}

class GetAnnouncementQuickResultsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(query: String) =
        userPreferencesRepository.userPreferences.map { userPreferences ->
            userPreferences.sortType
        }.distinctUntilChanged().flatMapLatest { sortType ->
            announcementRepository.getAnnouncementsQuickResults(sortType, query).map {
                it.toImmutableList()
            }
        }
}

class GetAnnouncementWithIdUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    operator fun invoke(id: Int): Flow<Announcement?> =
        announcementRepository.getAnnouncementWithId(id)
}


class SyncAnnouncementWithIdUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(id: Int) = announcementRepository.syncAnnouncementWithId(id)
}

class SetAnnouncementCheckTimeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() {
        userPreferencesRepository.setLastCheckTime(Clock.System.now())
    }
}


class CheckNewAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val tagsRepository: TagsRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(): Result<List<Announcement>, NetworkError> {
        val subscriptionResult = tagsRepository.syncSubscribedTags()

        if (subscriptionResult is Result.Error && subscriptionResult.error is NetworkError.Authentication) {
            return subscriptionResult
        }

        val subscribedTagIds = tagsRepository.subscribedTags.first().map { it.id }

        val lastNotifiedTime =
            userPreferencesRepository.userPreferences.first().lastNotificationCheckTime

        if (subscribedTagIds.isEmpty() || lastNotifiedTime == null) {
            userPreferencesRepository.setLastCheckTime(Clock.System.now())
            return Result.Success(emptyList())
        }

        val announcementResult = announcementRepository.fetchAnnouncements(
            page = 1,
            perPage = 20,
            sortType = SortType.DESC,
            tagIds = subscribedTagIds,
            updatedAfter = lastNotifiedTime
        )

        when (announcementResult) {
            is Result.Success -> {
                userPreferencesRepository.setLastCheckTime(Clock.System.now())
                return Result.Success(announcementResult.data)
            }

            is Result.Error -> {
                return announcementResult
            }
        }
    }
}