package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.NotificationRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke() = userPreferencesRepository.userPreferences
}

class SetHasSkippedSignInUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(hasSkippedSignIn: Boolean) =
        userPreferencesRepository.setSkippedSignIn(hasSkippedSignIn)
}

class SetThemeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(theme: Theme) =
        userPreferencesRepository.setTheme(theme)
}

class SetDynamicColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean) =
        userPreferencesRepository.setDynamicColor(enabled)
}

class ShowSignInNoticeRationaleUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return combine(
            authenticationRepository.isSignedIn,
            userPreferencesRepository.userPreferences
        ) { isSignedIn, userPreferences ->
            !isSignedIn && !userPreferences.hasSkippedSignedIn
        }
    }
}

class SetSortTypeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(sortType: SortType) =
        userPreferencesRepository.setSortType(sortType)
}

class SetSearchScopeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(searchScope: SearchScope) =
        userPreferencesRepository.setSearchScope(searchScope)
}

class IsForYouAvailableUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        combine(
            tagsRepository.subscribedTags,
            authenticationRepository.isSignedIn,
        ) { subscriptions, isSignedIn ->
            isSignedIn && subscriptions.isNotEmpty()
        }
}


class IsForYouEnabledUseCase @Inject constructor(
    private val isForYouAvailableUseCase: IsForYouAvailableUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        combine(
            isForYouAvailableUseCase(),
            userPreferencesRepository.userPreferences
        ) { isForYouAvailable, userPreferences ->
            isForYouAvailable && userPreferences.isForYouEnabled
        }
}


class SetForYouEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(value: Boolean) =
        userPreferencesRepository.setForYou(value)
}

class SetFabFiltersEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(value: Boolean) =
        userPreferencesRepository.setFabFilters(value)
}

class SetAnnouncementCheckIntervalUseCase @Inject constructor(
    private val workManager: WorkScheduler,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(minutes: Int) {
        workManager.scheduleAnnouncementAlerts(minutes)
        userPreferencesRepository.setCheckIntervalMinutes(minutes)
    }
}

class IncreaseImportantEventCountUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() {
        userPreferencesRepository.increaseImportantEventCount()
    }
}

class ResetImportantEventCountUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() {
        userPreferencesRepository.resetImportantEventCount()
    }
}

class ShouldShowReviewDialogUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        userPreferencesRepository.userPreferences.map {
            it.importantEventCount > 20
        }
}

class AreNotificationsAllowedUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(): Flow<Boolean> =
        notificationRepository.areNotificationsEnabled()
}


class IsAnnouncementCheckIntervalAvailableUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val notificationRepository: NotificationRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return combine(
            tagsRepository.subscribedTags,
            authenticationRepository.isSignedIn,
            notificationRepository.areNotificationsEnabled(),
        ) { tags, isSignedIn, areNotificationEnabled ->
            tags.isNotEmpty() && isSignedIn && areNotificationEnabled
        }
    }
}

class GetUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke() = userPreferencesRepository.userPreferences
}