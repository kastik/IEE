package com.kastik.usecases

import com.kastik.repository.UserPreferencesRepository

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