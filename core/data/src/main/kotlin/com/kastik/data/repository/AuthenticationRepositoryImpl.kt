package com.kastik.data.repository

import com.kastik.data.mappers.toDomain
import com.kastik.datastore.AuthenticationLocalDataSource
import com.kastik.model.aboard.AboardToken
import com.kastik.model.aboard.UserData
import com.kastik.model.aboard.UserProfile
import com.kastik.model.aboard.UserSubscribedTag
import com.kastik.model.apps.AppsToken
import com.kastik.network.datasource.AuthenticationRemoteDataSource
import com.kastik.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val local: AuthenticationLocalDataSource,
    private val remote: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override suspend fun exchangeCodeForAppsToken(code: String): AppsToken {
        val response = remote.exchangeCodeForAppsToken(code)
        local.saveAppsTokens(response.accessToken, response.refreshToken)
        return AppsToken(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            userId = response.userId
        )
    }

    override suspend fun exchangeCodeForAbroadToken(code: String): AboardToken {
        val response = remote.exchangeCodeForAboardToken(code)
        local.saveAboardToken((response.accessToken))
        local.saveAboardTokenExpiration(response.expiresIn)
        return AboardToken(
            accessToken = response.accessToken,
            tokenType = response.tokenType,
            userData = UserData(response.userData.id),
            expiresIn = response.expiresIn,
        )
    }

    override suspend fun checkIfUserIsAuthenticated(): Boolean {
        if (local.getAboardAccessToken() == null) {
            return false
        }

        val response = remote.checkIfTokenIsValid()
        return response
    }

    override suspend fun getSavedToken(): String? {
        return local.getAppsAccessToken()
    }

    override suspend fun getUserProfile(): UserProfile {
        return remote.getUserProfile().toDomain()
    }

    override suspend fun getUserSubscriptions(): List<UserSubscribedTag> {
        return remote.getUserSubscriptions().map { it.toDomain() }
    }
}