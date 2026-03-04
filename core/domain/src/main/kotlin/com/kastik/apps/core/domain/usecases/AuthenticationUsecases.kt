package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.error.NetworkError
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

class SignInUserUseCase @Inject constructor(
    private val workScheduler: WorkScheduler,
    private val tagsRepository: TagsRepository,
    private val profileRepository: ProfileRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(code: String) {
        authenticationRepository.exchangeCodeForAbroadToken(code)
        profileRepository.refreshProfile()
        tagsRepository.refreshSubscribedTags()
        workScheduler.scheduleAnnouncementAlerts()
    }
}

class RefreshIsSignedInUseCase @Inject constructor(
    private val signOutUserUseCase: SignOutUserUseCase,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(): Result<Unit, NetworkError> {
        if (!authenticationRepository.getIsSignedIn().first()) {
            return Result.Error(NetworkError.Authentication)
        }
        val result = authenticationRepository.refreshIsSignedIn()
        if (result is Result.Error && result.error is NetworkError.Authentication) {
            signOutUserUseCase()
        }
        return result
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
        userPreferencesRepository.setHasSkippedSignIn(false)
        workScheduler.cancelAnnouncementAlerts()
    }
}