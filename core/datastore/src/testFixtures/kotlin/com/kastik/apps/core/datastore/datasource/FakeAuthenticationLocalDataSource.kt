package com.kastik.apps.core.datastore.datasource

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthenticationLocalDataSource : AuthenticationLocalDataSource {

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    private val _aboardToken = MutableStateFlow<String?>(null)
    val aboardToken: StateFlow<String?> = _aboardToken.asStateFlow()

    override fun getIsSignedIn(): Flow<Boolean> {
        return isSignedIn
    }

    override fun getAboardAccessToken(): Flow<String?> = aboardToken

    override suspend fun setAboardAccessToken(accessToken: String) {
        _aboardToken.value = accessToken
    }

    override suspend fun setIsSignedIn(isSignedIn: Boolean) {
        _isSignedIn.value = isSignedIn
    }

    override suspend fun clearAuthenticationData() {
        _aboardToken.value = null
        _isSignedIn.value = false
    }

}
