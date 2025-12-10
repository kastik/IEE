package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnnouncementTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    suspend operator fun invoke(): Flow<List<Tag>> =
        tagsRepository.getAnnouncementTags()
}

class GetSubscribableTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    operator fun invoke(): Flow<List<SubscribableTag>> =
        tagsRepository.getSubscribableTags()
}

class RefreshSubscribableTagsUseCase @Inject constructor(
    private val tagsRepository: TagsRepository
) {
    suspend operator fun invoke() =
        tagsRepository.refreshSubscribableTags()
}