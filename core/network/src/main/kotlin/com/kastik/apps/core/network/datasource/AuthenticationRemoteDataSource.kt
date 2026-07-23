package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.model.response.TokenDto
import javax.inject.Inject
import javax.inject.Singleton

interface AuthenticationRemoteDataSource {
    suspend fun exchangeCodeForAboardToken(code: String): TokenDto
}

@Singleton
internal class AuthenticationRemoteDataSourceImpl
@Inject
constructor(@AuthenticatorAboardClient private val aboardApiClient: AboardApiClient) :
    AuthenticationRemoteDataSource {

    override suspend fun exchangeCodeForAboardToken(code: String): TokenDto =
        aboardApiClient.exchangeAuthCode(code = code)
}
