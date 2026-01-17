package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.apps.AppsAuthTokenDto
import com.kastik.apps.core.testing.testdata.aboardAuthTokenDtoTestData
import com.kastik.apps.core.testing.testdata.appsAuthTokenDtoTestData

class FakeAuthenticationRemoteDatasource : AuthenticationRemoteDataSource {

    private var aboardAccessTokenResponse: AboardAuthTokenDto? = null
    private var appsAccessTokenResponse: AppsAuthTokenDto? = null

    private var isTokenValid: Boolean = false

    var throwOnApiRequest: Throwable? = null

    fun setAboardAccessTokenResponse(token: AboardAuthTokenDto) {
        aboardAccessTokenResponse = token
    }

    fun setAppsAccessTokenResponse(token: AppsAuthTokenDto) {
        appsAccessTokenResponse = token
    }

    fun setTokenValidity(value: Boolean) {
        isTokenValid = value
    }


    override suspend fun exchangeCodeForAppsToken(code: String): AppsAuthTokenDto {
        throwOnApiRequest?.let { throw it }
        return appsAccessTokenResponse!!
    }

    override suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto {
        throwOnApiRequest?.let { throw it }
        return aboardAccessTokenResponse!!
    }

    override suspend fun checkIfTokenIsValid(): Boolean {
        throwOnApiRequest?.let { throw it }
        return isTokenValid
    }

    /* TODO MOVE TO PROFILE DATASOURCE
    override suspend fun getUserProfile() =
        if (aboardAccessToken != null) userProfilesTestData.first() else {
            throw Exception("Not logged in")
        }

    override suspend fun getUserSubscriptions() =
        if (aboardAccessToken != null) userSubscribedTagDtoListTestData else {
            throw Exception("Not logged in")
        }

     */
}
