package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kastik.apps.core.di.AuthDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


interface AuthenticationLocalDataSource {
    fun getIsSignedIn(): Flow<Boolean>
    fun getAboardAccessToken(): Flow<String?>
    fun getAboardTokenExpiration(): Flow<Int?>
    fun getAboardTokenLastRefreshTime(): Flow<Long?>
    suspend fun setIsSignedIn(isSignedIn: Boolean)
    suspend fun setAboardAccessToken(accessToken: String)
    suspend fun setAboardTokenExpiration(expiration: Int)
    suspend fun setAboardTokenLastRefreshTime(lastRefreshTime: Long)
    suspend fun clearAuthenticationData()

}

@Singleton
internal class AuthenticationLocalDataSourceImpl @Inject constructor(
    @AuthDatastore private val dataStore: DataStore<Preferences>
) : AuthenticationLocalDataSource {

    companion object {
        val IS_SIGNED_IN_KEY = booleanPreferencesKey("is_signed_in")
        val ABOARD_ACCESS_TOKEN_KEY = stringPreferencesKey("aboard_access_token")
        val ABOARD_ACCESS_TOKEN_EXPIRATION = intPreferencesKey("aboard_access_token_expiration")
        val ABOARD_ACCESS_TOKEN_LAST_REFRESH_TIME =
            longPreferencesKey("aboard_access_token_last_refresh_time")
    }

    override fun getIsSignedIn(): Flow<Boolean> =
        dataStore.data.map { it[IS_SIGNED_IN_KEY] ?: false }

    override fun getAboardAccessToken() =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_KEY] }

    override fun getAboardTokenExpiration() =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_EXPIRATION] }

    override fun getAboardTokenLastRefreshTime() =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_LAST_REFRESH_TIME] }


    override suspend fun setIsSignedIn(isSignedIn: Boolean) {
        dataStore.edit {
            it[IS_SIGNED_IN_KEY] = isSignedIn
        }
    }

    override suspend fun setAboardTokenExpiration(expiration: Int) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_EXPIRATION] = expiration
        }
    }

    override suspend fun setAboardTokenLastRefreshTime(lastRefreshTime: Long) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_LAST_REFRESH_TIME] = lastRefreshTime
        }
    }

    override suspend fun setAboardAccessToken(accessToken: String) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun clearAuthenticationData() {
        dataStore.edit { preferences ->
            preferences.remove(IS_SIGNED_IN_KEY)
            preferences.remove(ABOARD_ACCESS_TOKEN_KEY)
            preferences.remove(ABOARD_ACCESS_TOKEN_EXPIRATION)
            preferences.remove(ABOARD_ACCESS_TOKEN_LAST_REFRESH_TIME)
        }
    }

}