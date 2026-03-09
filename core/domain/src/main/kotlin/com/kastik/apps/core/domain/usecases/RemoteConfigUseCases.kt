package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.RemoteConfigRepository
import javax.inject.Inject

class FetchRemoteOptionsUseCase @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository,
) {
    suspend operator fun invoke() = remoteConfigRepository.fetchAndActivate()
}