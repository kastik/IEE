package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<Profile> = profileRepository.getProfile()
}

class GetSubscribedTagsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<ImmutableList<Tag>> =
        profileRepository.getEmailSubscriptions().map { it.toImmutableList() }
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


class SubscribeToTagsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(newTagIds: List<Int>) = coroutineScope {
        val currentTags = profileRepository.getEmailSubscriptions()
            .firstOrNull() ?: emptyList()

        val currentTagIds = currentTags.map { it.id }.toSet()
        val newTagIdsSet = newTagIds.toSet()

        val idsToSubscribe = newTagIdsSet - currentTagIds
        val idsToUnsubscribe = currentTagIds - newTagIdsSet

        launch {
            profileRepository.subscribeToEmailTags(newTagIds)
        }
        launch {
            profileRepository.subscribeToTopics(idsToSubscribe.toList())
        }
        launch {
            profileRepository.unsubscribeFromTopics(idsToUnsubscribe.toList())
        }
    }
}

class SignOutUserUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val announcementRepository: AnnouncementRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val workScheduler: WorkScheduler
) {
    suspend operator fun invoke() {
        profileRepository.clearLocalData()
        authenticationRepository.clearAuthenticationData()
        announcementRepository.clearAnnouncementCache()
        workScheduler.cancelTokenRefresh()
        try {
            profileRepository.unsubscribeFromAllTopics()
        } catch (e: ExecutionException) {
        }
    }
}