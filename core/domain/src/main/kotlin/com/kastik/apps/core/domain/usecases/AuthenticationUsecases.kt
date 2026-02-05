package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.service.WorkScheduler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIsSignedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Boolean> =
        authenticationRepository.getIsSignedIn()
}

class RefreshIsSignedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() =
        authenticationRepository.refreshIsSignedIn()
}

class ExchangeCodeForAboardTokenUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) =
        authenticationRepository.exchangeCodeForAbroadToken(code)
}

class StartTokenRefreshScheduleUseCase @Inject constructor(
    private val workScheduler: WorkScheduler
) {
    operator fun invoke() = workScheduler.scheduleTokenRefresh()
}