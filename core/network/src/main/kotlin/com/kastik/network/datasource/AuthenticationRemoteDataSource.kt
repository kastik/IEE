package com.kastik.network.datasource

import com.kastik.di.AnnRetrofit
import com.kastik.di.AuthRetrofit
import com.kastik.network.api.AboardApiClient
import com.kastik.network.api.AppsApiClient
import com.kastik.network.model.aboard.AboardAuthTokenDto
import com.kastik.network.model.aboard.UserProfileDto
import com.kastik.network.model.aboard.UserSubscribedTagDto
import com.kastik.network.model.apps.AppsAuthTokenDto


//TODO Removed this pre-commit
class AuthenticationRemoteDataSource(
    private val appsApiClient: AppsApiClient,
    private val aboardApiClient: AboardApiClient,
    private val clientId: String = "Secret 1",
    private val clientSecret: String = "Secret 2",
) {
    suspend fun exchangeCodeForAppsToken(code: String): AppsAuthTokenDto =
        appsApiClient.exchangeCodeForAppsToken(clientId, clientSecret, code = code)

    suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto =
        aboardApiClient.exchangeCodeForAboardToken(code = code)


    suspend fun checkIfTokenIsValid(): Boolean {
        return runCatching {
            aboardApiClient.getUserInfo()
        }.isSuccess
    }

    suspend fun getUserProfile(): UserProfileDto {
        return aboardApiClient.getUserInfo()
    }

    suspend fun getUserSubscriptions(): List<UserSubscribedTagDto> {
        return aboardApiClient.getUserSubscriptions()
    }
}