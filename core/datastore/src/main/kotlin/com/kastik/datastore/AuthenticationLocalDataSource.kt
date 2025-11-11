package com.kastik.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kastik.di.AuthPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthenticationLocalDataSource(
    @param:AuthPreferences private val dataStore: DataStore<Preferences>
) {
    companion object {
        val APPS_ACCESS_TOKEN_KEY = stringPreferencesKey("apps_access_token")
        val APPS_REFRESH_TOKEN_KEY = stringPreferencesKey("apps_refresh_token")
        val ABOARD_ACCESS_TOKEN_KEY = stringPreferencesKey("aboard_access_token")
        val ABOARD_ACCESS_TOKEN_EXPIRATION = intPreferencesKey("aboard_access_token_expiration")
    }

    suspend fun saveAppsTokens(accessToken: String, refreshToken: String?) {
        dataStore.edit {
            it[APPS_ACCESS_TOKEN_KEY] = accessToken
            refreshToken?.let { token -> it[APPS_REFRESH_TOKEN_KEY] = token }
        }
    }

    suspend fun getAppsAccessToken(): String? =
        dataStore.data.map { it[APPS_ACCESS_TOKEN_KEY] }.first()

    suspend fun saveAboardToken(accessToken: String) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun getAboardAccessToken(): String? =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_KEY] }.first()


    suspend fun saveAboardTokenExpiration(expiration: Int) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_EXPIRATION] = expiration
        }
    }

    suspend fun getAboardTokenExpiration(): Int? =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_EXPIRATION] }.first()

    fun getAboardAccessTokenFlow(): Flow<String?> =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_KEY] }

}