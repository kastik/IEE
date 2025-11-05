package com.kastik.appsaboard.data.datasource.local


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthenticationLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        Log.d("MyLog", "Saving tokens")
        dataStore.edit {
            it[ACCESS_TOKEN_KEY] = accessToken
            refreshToken?.let { token -> it[REFRESH_TOKEN_KEY] = token }
        }
    }

    suspend fun getAccessToken(): String? =
        dataStore.data.map { it[ACCESS_TOKEN_KEY] }.first()

}
