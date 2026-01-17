package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthenticationLocalDataSource : AuthenticationLocalDataSource {

    private val _aboardToken = MutableStateFlow<String?>(null)
    val aboardToken: StateFlow<String?> = _aboardToken.asStateFlow()

    private val _aboardTokenExpiration = MutableStateFlow<Int?>(null)
    val aboardTokenExpiration: StateFlow<Int?> = _aboardTokenExpiration.asStateFlow()

    private val _appsRefreshToken = MutableStateFlow<String?>(null)
    val refreshToken = _appsRefreshToken.asStateFlow()

    private val _appsToken = MutableStateFlow<String?>(null)
    val appsToken: StateFlow<String?> = _aboardToken.asStateFlow()


    override fun getAboardAccessTokenFlow(): Flow<String?> = aboardToken

    fun emitToken(newToken: String?) {
        _aboardToken.value = newToken
    }


    override suspend fun saveAboardToken(accessToken: String) {
        _aboardToken.value = accessToken
    }

    override suspend fun getAboardAccessToken(): String? {
        return _aboardToken.value
    }

    override suspend fun saveAboardTokenExpiration(expiration: Int) {
        _aboardTokenExpiration.value = expiration
    }

    override suspend fun getAboardTokenExpiration(): Int? {
        return _aboardTokenExpiration.value
    }

    override suspend fun clearAboardToken() {
        _aboardToken.value = null
        _aboardTokenExpiration.value = null
    }

    override suspend fun saveAppsTokens(accessToken: String, refreshToken: String?) {
        _appsToken.value = accessToken
        _appsRefreshToken.value = refreshToken
    }

    override suspend fun getAppsAccessToken(): String? = _appsToken.value

    override suspend fun clearAppsToken() {
        _appsToken.value = null
        _appsRefreshToken.value = null
    }
}
