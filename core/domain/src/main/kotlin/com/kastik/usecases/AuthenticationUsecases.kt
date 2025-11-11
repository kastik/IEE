package com.kastik.usecases

import com.kastik.repository.AuthenticationRepository

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

class GetUserProfileUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke() = repository.getUserProfile()
}

class GetUserSubscriptionsUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke() = repository.getUserSubscriptions()
}