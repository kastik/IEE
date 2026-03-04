package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.auth.AboardTokenResponseDto
import javax.inject.Inject

interface AuthenticationRemoteDataSource {
    suspend fun exchangeCodeForAboardToken(code: String): AboardTokenResponseDto
    suspend fun checkIfTokenIsValid(): Boolean
}


internal class AuthenticationRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient,
) : AuthenticationRemoteDataSource {

    override suspend fun exchangeCodeForAboardToken(code: String): AboardTokenResponseDto =
        aboardApiClient.exchangeCodeForAboardToken(code = code)

    override suspend fun checkIfTokenIsValid(): Boolean {
            aboardApiClient.getUserInfo()
        return true
    }

}