package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke() = profileRepository.profile
}

class RefreshProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() = profileRepository.refreshProfile()
}