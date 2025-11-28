package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AuthenticationRepository

class ExchangeCodeForAppsTokenUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) = repository.exchangeCodeForAppsToken(code)
}

class ExchangeCodeForAboardTokenUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) = repository.exchangeCodeForAbroadToken(code)
}

class CheckIfUserIsAuthenticatedUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke() = repository.checkIfUserIsAuthenticated()
}