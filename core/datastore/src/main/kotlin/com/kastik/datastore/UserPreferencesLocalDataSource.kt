package com.kastik.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.kastik.di.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesLocalDataSource(
    @param:UserPreferences private val dataStore: DataStore<Preferences>
) {
    companion object {
        val HAS_SKIPPED_SIGN_IN = booleanPreferencesKey("has_skipped_sign_in")
    }

    suspend fun getHasSkippedSignIn(): Boolean =
        dataStore.data.map { it[HAS_SKIPPED_SIGN_IN] }.first() ?: false

    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        dataStore.edit {
            it[HAS_SKIPPED_SIGN_IN] = hasSkippedSignIn
        }
    }

}