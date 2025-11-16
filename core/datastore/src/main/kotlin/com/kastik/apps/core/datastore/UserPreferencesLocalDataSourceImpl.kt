package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.kastik.apps.core.di.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface UserPreferencesLocalDataSource {
    suspend fun getHasSkippedSignIn(): Boolean
    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean)
}

class UserPreferencesLocalDataSourceImpl(
    @param:UserPreferences private val dataStore: DataStore<Preferences>
) : UserPreferencesLocalDataSource {
    companion object {
        val HAS_SKIPPED_SIGN_IN = booleanPreferencesKey("has_skipped_sign_in")
    }

    override suspend fun getHasSkippedSignIn(): Boolean =
        dataStore.data.map { it[HAS_SKIPPED_SIGN_IN] }.first() ?: false

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        dataStore.edit {
            it[HAS_SKIPPED_SIGN_IN] = hasSkippedSignIn
        }
    }

}