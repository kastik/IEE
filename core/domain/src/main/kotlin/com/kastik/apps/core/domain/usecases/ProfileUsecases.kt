package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke() = profileRepository.getProfile()
}

class GetSubscribedTagsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke() = profileRepository.getEmailSubscriptions().map { it.toImmutableList() }
}

class RefreshUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() = profileRepository.refreshProfile()
}

class RefreshSubscriptionsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() = profileRepository.refreshEmailSubscriptions()
}


class SubscribeToTagsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(newTagIds: List<Int>) = coroutineScope {
        val _ = profileRepository.getEmailSubscriptions().firstOrNull() ?: emptyList()

        val result = profileRepository.subscribeToEmailTags(newTagIds)

        //val currentTagIds = currentTags.map { it.id }.toSet()
        //val newTagIdsSet = newTagIds.toSet()

        //val idsToSubscribe = newTagIdsSet - currentTagIds
        //val idsToUnsubscribe = currentTagIds - newTagIdsSet

        userPreferencesRepository.setNotifiedAnnouncementId(emptyList())

        profileRepository.unsubscribeFromAllTopics()
        // val subscribeToTopicsDeferred = async { profileRepository.subscribeToTopics(idsToSubscribe.toList()) }
        //val unsubscribeFromTopicsDeferred = async { profileRepository.unsubscribeFromTopics(idsToUnsubscribe.toList()) }
        //awaitAll(subscribeToTopicsDeferred, unsubscribeFromTopicsDeferred)
        result
    }
}