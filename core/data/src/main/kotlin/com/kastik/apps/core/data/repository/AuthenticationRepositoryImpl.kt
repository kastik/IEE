package com.kastik.apps.core.data.repository

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override suspend fun exchangeCodeForAppsToken(code: String) = withContext(Dispatchers.IO) {
        val response = authenticationRemoteDataSource.exchangeCodeForAppsToken(code)
        authenticationLocalDataSource.saveAppsTokens(response.accessToken, response.refreshToken)
    }

    override suspend fun exchangeCodeForAbroadToken(code: String) = withContext(Dispatchers.IO) {
        val response = authenticationRemoteDataSource.exchangeCodeForAboardToken(code)
        authenticationLocalDataSource.saveAboardToken((response.accessToken))
        authenticationLocalDataSource.saveAboardTokenExpiration(response.expiresIn)
    }

    override suspend fun getAboardToken(): String? {
        return authenticationLocalDataSource.getAboardAccessToken()
    }

    override suspend fun checkAboardTokenValidity(): Boolean {
        if (authenticationLocalDataSource.getAboardAccessToken() == null) {
            return false
        }

        val response = authenticationRemoteDataSource.checkIfTokenIsValid()
        return response
    }


    override suspend fun clearTokens() = withContext(Dispatchers.IO) {
        authenticationLocalDataSource.clearAboardToken()
        authenticationLocalDataSource.clearAppsToken()
    }

}