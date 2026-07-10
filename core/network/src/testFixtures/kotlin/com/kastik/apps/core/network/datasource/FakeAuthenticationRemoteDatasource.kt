package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.FakeAboardApiClient
import com.kastik.apps.core.network.model.response.TokenDto

class FakeAuthenticationRemoteDatasource : AuthenticationRemoteDataSource {

    var isTokenValid: Boolean = false
    var throwOnApiRequest: Throwable? = null
    var aboardAccessTokenResponse: TokenDto? = null

    val fakeAboardApiClient = FakeAboardApiClient()

    override suspend fun exchangeCodeForAboardToken(code: String): TokenDto {
        throwOnApiRequest?.let { throw it }

        fakeAboardApiClient.exchangeAuthCode(code)

        aboardAccessTokenResponse?.let {
            return it
        }

        throw IllegalStateException("Aboard Access Token Response is null")
    }

    override suspend fun checkIfTokenIsValid(): Boolean {
        throwOnApiRequest?.let { throw it }
        fakeAboardApiClient.getCurrentUser() //TODO
        return isTokenValid
    }
}
