package com.kastik.apps.core.data.repository

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource

internal class AuthenticationRepositoryImpl(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override suspend fun exchangeCodeForAppsToken(code: String) {
        val response = authenticationRemoteDataSource.exchangeCodeForAppsToken(code)
        authenticationLocalDataSource.saveAppsTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun exchangeCodeForAbroadToken(code: String) {
        val response = authenticationRemoteDataSource.exchangeCodeForAboardToken(code)
        authenticationLocalDataSource.saveAboardToken((response.accessToken))
        authenticationLocalDataSource.saveAboardTokenExpiration(response.expiresIn)
    }

    override suspend fun checkTokenValidity(): Boolean {
        if (authenticationLocalDataSource.getAboardAccessToken() == null) {
            return false
        }

        val response = authenticationRemoteDataSource.checkIfTokenIsValid()
        return response
    }

    override suspend fun getAboardToken(): String? {
        return authenticationLocalDataSource.getAboardAccessToken()
    }

    override suspend fun clearTokens() {
        authenticationLocalDataSource.clearAboardToken()
        authenticationLocalDataSource.clearAppsToken()
    }

}