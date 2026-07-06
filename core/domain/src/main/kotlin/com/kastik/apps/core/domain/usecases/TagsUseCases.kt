package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.common.extensions.removeAccents
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.result.Result
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Clock

class GetAnnouncementTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke() = combine(
        tagsRepository.announcementTags,
        authenticationRepository.isSignedIn,
    ) { tags, isSignedIn ->
        if (isSignedIn) tags else tags.filter { it.isPublic }
    }.map { it.toImmutableList() }
}

class GetSubscribableTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    operator fun invoke() = tagsRepository.subscribableTags.map { it.toImmutableList() }
}

class GetSubscriptionsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
) {
    operator fun invoke() = tagsRepository.subscribedTags.map { it.toImmutableList() }
}

class SyncAnnouncementTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    suspend operator fun invoke() = tagsRepository.syncAnnouncementTags()
}


class SyncSubscribableTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    suspend operator fun invoke() = tagsRepository.syncSubscribableTags()
}


class SyncSubscriptionsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
) {
    suspend operator fun invoke() = tagsRepository.syncSubscribedTags()
}

class SubscribeToTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(newTagIds: List<Int>) = coroutineScope {
        val result = tagsRepository.subscribeToTags(newTagIds)
        if (result is Result.Success) {
            userPreferencesRepository.setLastCheckTime(Clock.System.now())
        }
        result
    }
}


class GetTagsQuickResultsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(query: String) = combine(
        tagsRepository.announcementTags,
        authenticationRepository.isSignedIn
    ) { tags, isSignedIn ->
        val normalizedQuery = query.removeAccents()

        tags.filter {
            (isSignedIn || it.isPublic) &&
                    it.title.removeAccents().contains(normalizedQuery, ignoreCase = true)
        }
            .take(5)
            .toImmutableList()
    }
}