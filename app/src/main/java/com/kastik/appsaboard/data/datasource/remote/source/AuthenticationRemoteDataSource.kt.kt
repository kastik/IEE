package com.kastik.appsaboard.data.datasource.remote.source

import com.kastik.appsaboard.data.datasource.remote.api.AuthApiClient
import com.kastik.appsaboard.data.datasource.remote.dto.AuthTokenDto

class AuthenticationRemoteDataSource(
    private val api: AuthApiClient,
    private val clientId: String = "secret1",
    private val clientSecret: String = "secret",
) {
    suspend fun exchangeCodeForToken(code: String): AuthTokenDto =
        api.exchangeCodeForToken(clientId, clientSecret, code = code)
}