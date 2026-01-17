package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AuthenticationRepository
import javax.inject.Inject

class ExchangeCodeForAppsTokenUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) =
        authenticationRepository.exchangeCodeForAppsToken(code)
}

class ExchangeCodeForAboardTokenUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) =
        authenticationRepository.exchangeCodeForAbroadToken(code)
}

class CheckIfTokenIsValidUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke() = authenticationRepository.checkAboardTokenValidity()
}