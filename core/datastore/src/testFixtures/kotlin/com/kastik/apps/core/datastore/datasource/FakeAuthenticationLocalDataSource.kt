package com.kastik.apps.core.datastore.datasource

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthenticationLocalDataSource() : AuthenticationLocalDataSource {

    private val _isSignedIn = MutableStateFlow(false)
    private val _aboardToken = MutableStateFlow<String?>(null)

    override val isSignedIn = _isSignedIn.asStateFlow()

    override val aboardAccessToken = _aboardToken.asStateFlow()

    override suspend fun setIsSignedIn(isSignedIn: Boolean) {
        _isSignedIn.value = isSignedIn
    }

    override suspend fun setAboardAccessToken(accessToken: String) {
        _aboardToken.value = accessToken
    }

    override suspend fun clearAuthenticationData() {
        _aboardToken.value = null
        _isSignedIn.value = false
    }
}
