package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.auth.AboardTokenRefreshRequestDto
import com.kastik.apps.core.network.model.aboard.auth.AboardTokenResponseDto
import javax.inject.Inject

interface AuthenticationRemoteDataSource {
    suspend fun exchangeCodeForAboardToken(code: String): AboardTokenResponseDto
    suspend fun refreshAboardToken(token: String): AboardTokenResponseDto
    suspend fun checkIfTokenIsValid(): Boolean
}


internal class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val aboardApiClient: AboardApiClient,
) : AuthenticationRemoteDataSource {

    override suspend fun exchangeCodeForAboardToken(code: String): AboardTokenResponseDto =
        aboardApiClient.exchangeCodeForAboardToken(code = code)

    override suspend fun refreshAboardToken(token: String): AboardTokenResponseDto {
        return aboardApiClient.refreshToken(AboardTokenRefreshRequestDto(token))
    }

    override suspend fun checkIfTokenIsValid(): Boolean {
        return runCatching {
            aboardApiClient.getUserInfo()
        }.isSuccess
    }

}