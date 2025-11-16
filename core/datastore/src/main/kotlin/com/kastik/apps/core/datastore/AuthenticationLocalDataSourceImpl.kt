package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kastik.apps.core.di.AuthPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


interface AuthenticationLocalDataSource {
    suspend fun saveAppsTokens(accessToken: String, refreshToken: String?)

    suspend fun getAppsAccessToken(): String?

    suspend fun saveAboardToken(accessToken: String)

    suspend fun getAboardAccessToken(): String?


    suspend fun saveAboardTokenExpiration(expiration: Int)

    suspend fun getAboardTokenExpiration(): Int?

    fun getAboardAccessTokenFlow(): Flow<String?>

}

class AuthenticationLocalDataSourceImpl(
    @param:AuthPreferences private val dataStore: DataStore<Preferences>
) : AuthenticationLocalDataSource {
    companion object {
        val APPS_ACCESS_TOKEN_KEY = stringPreferencesKey("apps_access_token")
        val APPS_REFRESH_TOKEN_KEY = stringPreferencesKey("apps_refresh_token")
        val ABOARD_ACCESS_TOKEN_KEY = stringPreferencesKey("aboard_access_token")
        val ABOARD_ACCESS_TOKEN_EXPIRATION = intPreferencesKey("aboard_access_token_expiration")
    }

    override suspend fun saveAppsTokens(accessToken: String, refreshToken: String?) {
        dataStore.edit {
            it[APPS_ACCESS_TOKEN_KEY] = accessToken
            refreshToken?.let { token -> it[APPS_REFRESH_TOKEN_KEY] = token }
        }
    }

    override suspend fun getAppsAccessToken(): String? =
        dataStore.data.map { it[APPS_ACCESS_TOKEN_KEY] }.first()

    override suspend fun saveAboardToken(accessToken: String) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun getAboardAccessToken(): String? =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_KEY] }.first()


    override suspend fun saveAboardTokenExpiration(expiration: Int) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_EXPIRATION] = expiration
        }
    }

    override suspend fun getAboardTokenExpiration(): Int? =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_EXPIRATION] }.first()

    override fun getAboardAccessTokenFlow(): Flow<String?> =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_KEY] }

}