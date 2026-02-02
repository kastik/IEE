package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.common.extensions.removeAccents
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.model.aboard.SubscribableTag
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAnnouncementTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke() = combine(
        tagsRepository.getAnnouncementTags(),
        authenticationRepository.getIsSignedIn(),
    ) { tags, isSignedIn ->
        if (isSignedIn) tags else tags.filter { it.isPublic }
    }.map { it.toImmutableList() }
}

class RefreshAnnouncementTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    suspend operator fun invoke() = tagsRepository.refreshAnnouncementTags()
}

class GetSubscribableTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    operator fun invoke(): Flow<ImmutableList<SubscribableTag>> =
        tagsRepository.getSubscribableTags().map { it.toImmutableList() }
}

class RefreshSubscribableTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    suspend operator fun invoke() = tagsRepository.refreshSubscribableTags()
}


class GetTagsQuickResults @Inject constructor(
    private val tagsRepository: TagsRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    operator fun invoke(query: String) = combine(
        tagsRepository.getAnnouncementTags(),
        authenticationRepository.getIsSignedIn()
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