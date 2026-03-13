package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.common.extensions.combine
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.NotificationRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserPreferences
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class HasSkippedSignInUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        userPreferencesRepository.hasSkippedSignIn()
}

class SetHasSkippedSignInUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(hasSkippedSignIn: Boolean) =
        userPreferencesRepository.setHasSkippedSignIn(hasSkippedSignIn)
}

class GetThemeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserTheme> =
        userPreferencesRepository.getTheme()
}

class SetThemeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(theme: UserTheme) =
        userPreferencesRepository.setTheme(theme)
}


class GetDynamicColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        userPreferencesRepository.isDynamicColorEnabled()
}

class SetDynamicColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean) =
        userPreferencesRepository.setDynamicColorEnabled(enabled)
}

class ShowSignInNoticeRationalUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return combine(
            authenticationRepository.getIsSignedIn(),
            userPreferencesRepository.hasSkippedSignIn()
        ) { isSignedIn, hasSkipped ->
            !isSignedIn && !hasSkipped
        }
    }
}

class GetSortTypeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<SortType> =
        userPreferencesRepository.getSortType()
}

class SetSortTypeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(sortType: SortType) =
        userPreferencesRepository.setSortType(sortType)
}


class GetSearchScopeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<SearchScope> =
        userPreferencesRepository.getSearchScope()
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
            tagsRepository.getSubscribedTags(),
            authenticationRepository.getIsSignedIn(),
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
            userPreferencesRepository.isForYouEnabled()
        ) { isForYouAvailable, isForYouEnabled ->
            isForYouAvailable && isForYouEnabled
        }
}

class SetForYouEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(value: Boolean) =
        userPreferencesRepository.setForYouEnabled(value)
}

class AreFabFiltersEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        userPreferencesRepository.areFabFiltersEnabled()
}

class SetFabFiltersEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(value: Boolean) =
        userPreferencesRepository.setFabFiltersEnabled(value)
}


class GetAnnouncementCheckIntervalMinutesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Int> =
        userPreferencesRepository.getAnnouncementCheckIntervalMinutes()
}

class SetAnnouncementCheckIntervalUseCase @Inject constructor(
    private val workManager: WorkScheduler,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(minutes: Int) {
        workManager.scheduleAnnouncementAlerts(minutes)
        userPreferencesRepository.setAnnouncementCheckIntervalMinutes(minutes)
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
            tagsRepository.getSubscribedTags(),
            authenticationRepository.getIsSignedIn(),
            notificationRepository.areNotificationsEnabled(),
        ) { tags, isSignedIn, areNotificationEnabled ->
            tags.isNotEmpty() && isSignedIn && areNotificationEnabled
        }
    }
}

class GetUserPreferencesUseCase @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val getDynamicColorUseCase: GetDynamicColorUseCase,
    private val getSortTypeUseCase: GetSortTypeUseCase,
    private val getSearchScopeUseCase: GetSearchScopeUseCase,
    private val isForYouEnabledUseCase: IsForYouEnabledUseCase,
    private val areFabFiltersEnabledUseCase: AreFabFiltersEnabledUseCase,
    private val getAnnouncementCheckIntervalMinutesUseCase: GetAnnouncementCheckIntervalMinutesUseCase,
) {
    operator fun invoke() =
        combine(
            getThemeUseCase(),
            getDynamicColorUseCase(),
            getSortTypeUseCase(),
            getSearchScopeUseCase(),
            isForYouEnabledUseCase(),
            areFabFiltersEnabledUseCase(),
            getAnnouncementCheckIntervalMinutesUseCase(),
        ) { theme, dynamicColor, sortType, searchScope, enableForYou, disableFabFilters, announcementCheckIntervalHours ->
            UserPreferences(
                theme,
                dynamicColor,
                sortType,
                searchScope,
                enableForYou,
                disableFabFilters,
                announcementCheckIntervalHours
            )
        }
}


//TODO This is a work around for not yet set user properties on older versions,
//Remove this after enough users migrate to newer versions as analytics calls don't belong in domain layer
class UpdateUserPropertiesUseCase @Inject constructor(
    private val analytics: Analytics,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
) {
    suspend operator fun invoke() {
        val prefs = getUserPreferencesUseCase().first()
        analytics.setUserProperty("theme", prefs.theme.name)
        analytics.setUserProperty("dynamic_color", prefs.dynamicColor.toString())
        analytics.setUserProperty("sort_type", prefs.sortType.name)
        analytics.setUserProperty("search_scope", prefs.searchScope.name)
        analytics.setUserProperty("for_you", prefs.enableForYou.toString())
        analytics.setUserProperty("fab_filters", prefs.disableFabFilters.toString())
    }
}