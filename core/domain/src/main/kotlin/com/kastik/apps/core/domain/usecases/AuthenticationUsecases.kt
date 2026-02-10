package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.AuthenticationError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetIsSignedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Boolean> =
        authenticationRepository.getIsSignedIn()
}

class RefreshIsSignedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Result<Unit, AuthenticatedRefreshError> {
        val wasSignedIn = authenticationRepository.getIsSignedIn().first()
        val result = authenticationRepository.refreshIsSignedIn()
        val isNowSignedIn = authenticationRepository.getIsSignedIn().first()
        if (wasSignedIn && !isNowSignedIn) {
            userPreferencesRepository.setHasSkippedSignIn(false)
        }
        return result
    }
}

class LoginUserUseCase @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val profileRepository: ProfileRepository,
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) {
        authenticationRepository.exchangeCodeForAbroadToken(code)
        profileRepository.refreshProfile()
        profileRepository.refreshEmailSubscriptions()
        workScheduler.scheduleTokenRefresh()
        workScheduler.scheduleAnnouncementAlerts()
    }
}

class SignOutUserUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val announcementRepository: AnnouncementRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val workScheduler: WorkScheduler
) {
    suspend operator fun invoke() {
        //TODO Make this also return Result
        profileRepository.clearLocalData()
        authenticationRepository.clearAuthenticationData()
        announcementRepository.clearAnnouncementCache()
        userPreferencesRepository.clearNotifiedAnnouncementId()
        workScheduler.cancelTokenRefresh()
        workScheduler.cancelAnnouncementAlerts()
        profileRepository.unsubscribeFromAllTopics()
    }
}

class RefreshTokenUseCase @Inject constructor(
    private val repository: AuthenticationRepository,
    private val signOutUserUseCase: SignOutUserUseCase
) {
    suspend operator fun invoke(): Result<Unit, AuthenticatedRefreshError> {
        val result = repository.refreshAboardToken()
        if (result is Result.Error && result.error is AuthenticationError) {
            signOutUserUseCase()
        }
        return result
    }
}