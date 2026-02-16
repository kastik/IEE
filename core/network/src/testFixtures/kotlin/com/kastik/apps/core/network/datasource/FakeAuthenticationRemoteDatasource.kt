package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.model.aboard.auth.AboardTokenResponseDto

class FakeAuthenticationRemoteDatasource : AuthenticationRemoteDataSource {

    var isTokenValid: Boolean = false
    var throwOnApiRequest: Throwable? = null
    var aboardAccessTokenResponse: AboardTokenResponseDto? = null

    override suspend fun exchangeCodeForAboardToken(code: String): AboardTokenResponseDto {
        throwOnApiRequest?.let { throw it }
        aboardAccessTokenResponse?.let {
            return it
        }
        throw IllegalStateException("Aboard Access Token Response is null")
    }

    override suspend fun refreshAboardToken(token: String): AboardTokenResponseDto {
        throwOnApiRequest?.let { throw it }
        aboardAccessTokenResponse?.let {
            return it
        }
        throw IllegalStateException("Aboard Access Token Response is null")
    }

    override suspend fun checkIfTokenIsValid(): Boolean {
        throwOnApiRequest?.let { throw it }
        return isTokenValid
    }
}
