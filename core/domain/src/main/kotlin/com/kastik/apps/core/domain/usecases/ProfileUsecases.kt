package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke() = profileRepository.getProfile()
}

class RefreshUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(): Result<Unit, NetworkError> {
        return profileRepository.refreshProfile()
    }
}