package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toDomain
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.model.aboard.UserProfile
import com.kastik.apps.core.model.aboard.UserSubscribedTag
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource

class AuthenticationRepositoryImpl(
    private val local: AuthenticationLocalDataSource,
    private val remote: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override suspend fun exchangeCodeForAppsToken(code: String) {
        val response = remote.exchangeCodeForAppsToken(code)
        local.saveAppsTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun exchangeCodeForAbroadToken(code: String) {
        val response = remote.exchangeCodeForAboardToken(code)
        local.saveAboardToken((response.accessToken))
        local.saveAboardTokenExpiration(response.expiresIn)
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