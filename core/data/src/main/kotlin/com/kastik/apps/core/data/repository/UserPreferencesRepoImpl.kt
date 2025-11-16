package com.kastik.apps.core.data.repository

import com.kastik.apps.core.datastore.UserPreferencesLocalDataSource
import com.kastik.apps.core.di.UserPreferences
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UserPreferencesRepoImpl @Inject constructor(
    @param:UserPreferences private val userPreferences: UserPreferencesLocalDataSource
) : UserPreferencesRepository {
    override suspend fun getHasSkippedSignIn(): Boolean {
        return userPreferences.getHasSkippedSignIn()
    }

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        return userPreferences.setHasSkippedSignIn(hasSkippedSignIn)
    }
}