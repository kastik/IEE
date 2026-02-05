package com.kastik.apps.core.domain.usecases

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

class GetUserHasSkippedSignInUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        userPreferencesRepository.getHasSkippedSignIn()
}

class SetUserHasSkippedSignInUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(hasSkippedSignIn: Boolean) =
        userPreferencesRepository.setHasSkippedSignIn(hasSkippedSignIn)
}

class GetUserThemeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserTheme> =
        userPreferencesRepository.getUserTheme()
}

class SetUserThemeUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(theme: UserTheme) =
        userPreferencesRepository.setUserTheme(theme)
}


class GetDynamicColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        userPreferencesRepository.getDynamicColor()
}

class SetDynamicColorUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean) =
        userPreferencesRepository.setDynamicColor(enabled)
}

class ShowSignInNoticeRationalUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke(): Flow<Boolean> {
        return combine(
            authenticationRepository.getIsSignedIn(),
            userPreferencesRepository.getHasSkippedSignIn()
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


class GetEnableForYouUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        combine(
            profileRepository.getEmailSubscriptions(),
            userPreferencesRepository.getEnableForYou()
        ) { subscriptions, enableForYou ->
            subscriptions.isNotEmpty() && enableForYou
        }
}

class SetEnableForYouUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(value: Boolean) =
        userPreferencesRepository.setEnableForYou(value)
}

class GetUserPreferencesUseCase @Inject constructor(
    private val getUserThemeUseCase: GetUserThemeUseCase,
    private val getDynamicColorUseCase: GetDynamicColorUseCase,
    private val getSortTypeUseCase: GetSortTypeUseCase,
    private val getSearchScopeUseCase: GetSearchScopeUseCase,
    private val getEnableForYouUseCase: GetEnableForYouUseCase,
) {
    operator fun invoke() =
        combine(
            getUserThemeUseCase(),
            getDynamicColorUseCase(),
            getSortTypeUseCase(),
            getSearchScopeUseCase(),
            getEnableForYouUseCase(),
        ) { theme, dynamicColor, sortType, searchScope, enableForYou ->
            UserPreferences(theme, dynamicColor, sortType, searchScope, enableForYou)
        }
}
