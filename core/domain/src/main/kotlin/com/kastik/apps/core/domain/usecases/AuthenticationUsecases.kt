package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first

class GetIsSignedInUseCase
@Inject
constructor(private val authenticationRepository: AuthenticationRepository) {
    operator fun invoke() = authenticationRepository.isSignedIn
}

class SignInUseCase
@Inject
constructor(
    private val workScheduler: WorkScheduler,
    private val tagsRepository: TagsRepository,
    private val profileRepository: ProfileRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(code: String): Result<Unit, NetworkError> {
        val authResult = authenticationRepository.signIn(code)
        if (authResult is Result.Error) {
            return authResult
        }

        coroutineScope {
            val profileSync = async { profileRepository.syncProfile() }
            val subscribedSync = async { tagsRepository.syncSubscribedTags() }
            val announcementSync = async { tagsRepository.syncAnnouncementTags() }
            val subscribableSync = async { tagsRepository.syncSubscribableTags() }
            awaitAll(profileSync, subscribedSync, announcementSync, subscribableSync)
        }

        val interval = userPreferencesRepository.userPreferences.first().checkIntervalMinutes
        workScheduler.scheduleAnnouncementAlerts(interval)

        return Result.Success(Unit)
    }
}

class SignOutUseCase
@Inject
constructor(
    private val workScheduler: WorkScheduler,
    private val profileRepository: ProfileRepository,
    private val announcementRepository: AnnouncementRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke() {
        profileRepository.clearProfile()
        authenticationRepository.signOut()
        announcementRepository.clearAnnouncementCache()

        userPreferencesRepository.setSkippedSignIn(false)
        workScheduler.cancelAnnouncementAlerts()
    }
}

class TriggerSignOutOnStatusChangeUseCase
@Inject
constructor(
    private val signOutUseCase: SignOutUseCase,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke() {
        var wasSignedIn = false
        authenticationRepository.isSignedIn.distinctUntilChanged().collect { isSignedIn ->
            if (wasSignedIn && !isSignedIn) {
                signOutUseCase()
            }
            wasSignedIn = isSignedIn
        }
    }
}
