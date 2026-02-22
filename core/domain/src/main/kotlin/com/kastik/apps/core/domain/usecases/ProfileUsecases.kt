package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.result.Result
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Clock

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
    private val refreshIsSignedInUseCase: RefreshIsSignedInUseCase,
) {
    suspend operator fun invoke(): Result<Unit, AuthenticatedRefreshError> {
        refreshIsSignedInUseCase()
        return profileRepository.refreshProfile()
    }
}

class RefreshEmailSubscriptionsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() =
        profileRepository.refreshEmailSubscriptions()
}

class SyncTopicSubscriptionsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() = profileRepository.syncTopicSubscriptions()
}


class SubscribeToEmailTagsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke(newTagIds: List<Int>) = coroutineScope {
        val result = profileRepository.subscribeToEmailTags(newTagIds)
        if (result is Result.Success) {
            userPreferencesRepository.setLastNotificationCheckTime(Clock.System.now())
        }
        result
    }
}

class ScheduleSubscribeToTagsUseCase @Inject constructor(
    private val workScheduler: WorkScheduler
) {
    operator fun invoke(newTagIds: List<Int>) {
        workScheduler.scheduleSubscribeToTags(newTagIds)
    }
}

class GetSubscribeToTagsWorkInfoUseCase @Inject constructor(
    private val workScheduler: WorkScheduler
) {
    operator fun invoke() =
        workScheduler.getSubscribeToTagsWorkInfo()

}