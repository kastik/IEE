package com.kastik.data.repository

import com.kastik.datastore.UserPreferencesLocalDataSource
import com.kastik.di.UserPreferences
import com.kastik.repository.UserPreferencesRepository
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