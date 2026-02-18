package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.common.extensions.combine
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserPreferences
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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


class IsForYouEnabledUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        combine(
            profileRepository.getEmailSubscriptions(),
            userPreferencesRepository.isForYouEnabled()
        ) { subscriptions, enableForYou ->
            subscriptions.isNotEmpty() && enableForYou
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

class GetUserPreferencesUseCase @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val getDynamicColorUseCase: GetDynamicColorUseCase,
    private val getSortTypeUseCase: GetSortTypeUseCase,
    private val getSearchScopeUseCase: GetSearchScopeUseCase,
    private val isForYouEnabledUseCase: IsForYouEnabledUseCase,
    private val areFabFiltersEnabledUseCase: AreFabFiltersEnabledUseCase,
) {
    operator fun invoke() =
        combine(
            getThemeUseCase(),
            getDynamicColorUseCase(),
            getSortTypeUseCase(),
            getSearchScopeUseCase(),
            isForYouEnabledUseCase(),
            areFabFiltersEnabledUseCase(),
        ) { theme, dynamicColor, sortType, searchScope, enableForYou, disableFabFilters ->
            UserPreferences(
                theme,
                dynamicColor,
                sortType,
                searchScope,
                enableForYou,
                disableFabFilters
            )
        }
}

class StoreNotifiedAnnouncementIdsUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(ids: List<Int>) {
        userPreferencesRepository.setNotifiedAnnouncementId(ids)
    }

    suspend operator fun invoke(id: Int) {
        userPreferencesRepository.setNotifiedAnnouncementId(id)
    }
}