package com.kastik.appsaboard.domain.usecases

import com.kastik.appsaboard.domain.repository.AuthenticationRepository

class ExchangeCodeForTokenUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) = repository.exchangeCodeForToken(code)
}