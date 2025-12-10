package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.aboard.Profile
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutionException
import javax.inject.Inject


class GetIsSignedInUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<Boolean> =
        profileRepository.isSignedIn()
}

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<Profile> {
        try {
            return profileRepository.getProfile()
        } catch (e: Exception) {
            //firebaseRepo.unsubscribeFromAllTopics()
            throw e
        }
    }
}

class RefreshUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        try {
            profileRepository.refreshProfile()
        } catch (e: Exception) {
            //firebaseRepo.unsubscribeFromAllTopics()
            throw e
        }
    }
}

class RefreshSubscriptionsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        try {
            profileRepository.refreshEmailSubscriptions()
        } catch (e: Exception) {
            //firebaseRepo.unsubscribeFromAllTopics()
            throw e
        }
    }
}

class GetSubscriptionsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke() = profileRepository.getEmailSubscriptions()
}

class SubscribeToTagsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(tags: List<Int>) {
        profileRepository.subscribeToEmailTags(tags)
        profileRepository.subscribeToTopics(tags)
    }
}

class SignOutUserUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val announcementRepository: AnnouncementRepository,
    private val authenticationRepository: AuthenticationRepository,

    ) {
    suspend operator fun invoke() {
        profileRepository.clearLocalData()
        authenticationRepository.clearTokens()
        announcementRepository.clearAnnouncementCache()
        try {
            profileRepository.unsubscribeFromAllTopics()
        } catch (e: ExecutionException) {
        }

    }
}