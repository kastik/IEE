package com.kastik.apps.core.network.interceptor


class FakeTokenManager : TokenManager {
    private var token: String? = null

    override suspend fun getToken(): String? = token

    override suspend fun updateToken(newToken: String) {
        token = newToken
    }

}
