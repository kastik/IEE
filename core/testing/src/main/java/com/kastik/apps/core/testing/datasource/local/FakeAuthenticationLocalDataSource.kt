package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthenticationLocalDataSource(
    initialToken: String? = null
) : AuthenticationLocalDataSource {

    private val tokenFlow = MutableStateFlow(initialToken)

    var savedAppsAccessToken: String? = null
    var savedAppsRefreshToken: String? = null

    var savedAboardAccessToken: String? = initialToken
        private set

    var savedAboardExpiration: Int? = null


    override fun getAboardAccessTokenFlow(): Flow<String?> = tokenFlow

    fun emitToken(newToken: String?) {
        savedAboardAccessToken = newToken
        tokenFlow.value = newToken
    }


    override suspend fun saveAboardToken(accessToken: String) {
        savedAboardAccessToken = accessToken
        tokenFlow.value = accessToken
    }

    override suspend fun getAboardAccessToken(): String? {
        return savedAboardAccessToken
    }

    override suspend fun saveAboardTokenExpiration(expiration: Int) {
        savedAboardExpiration = expiration
    }

    override suspend fun getAboardTokenExpiration(): Int? {
        return savedAboardExpiration
    }

    override suspend fun saveAppsTokens(accessToken: String, refreshToken: String?) {
        savedAppsAccessToken = accessToken
        savedAppsRefreshToken = refreshToken
    }

    override suspend fun getAppsAccessToken(): String? =
        savedAppsAccessToken
}
