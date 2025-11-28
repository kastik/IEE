package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.UserInfoRepository
import com.kastik.apps.core.model.aboard.UserSubscribableTag


class GetUserProfileUseCase(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke() = repository.getUserProfile()
}

class GetUserSubscriptionsUseCase(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke() = repository.getUserSubscriptions()
}

class GetUserSubscribableTagsUseCase(
    private val repo: UserInfoRepository
) {
    suspend operator fun invoke(): List<UserSubscribableTag> =
        repo.getUserSubscribableTags()
}

class SubscribeToTagsUseCase(
    private val repo: UserInfoRepository
) {
    suspend operator fun invoke(tags: List<Int>) =
        repo.subscribeToTags(tags)
}