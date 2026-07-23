package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke() = profileRepository.profile
}

class GetUserIdUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    operator fun invoke() = profileRepository.profile.map { it?.uid }
}

class SyncProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke() = profileRepository.syncProfile()
}
