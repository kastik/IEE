package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.UserPreferencesRepository

class CheckIfUserHasSkippedSignInUseCase(
    private val repo: UserPreferencesRepository
) {
    suspend operator fun invoke(): Boolean =
        repo.getHasSkippedSignIn()
}

class SetUserHasSkippedSignInUseCase(
    private val repo: UserPreferencesRepository
) {
    suspend operator fun invoke(hasSkippedSignIn: Boolean) =
        repo.setHasSkippedSignIn(hasSkippedSignIn)
}