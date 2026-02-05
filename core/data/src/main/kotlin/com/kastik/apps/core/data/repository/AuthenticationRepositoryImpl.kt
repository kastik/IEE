package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthenticationRepository {

    override fun getIsSignedIn(): Flow<Boolean> =
        authenticationLocalDataSource.getIsSignedIn()

    override suspend fun refreshIsSignedIn() {
        authenticationRemoteDataSource.checkIfTokenIsValid().let {
            authenticationLocalDataSource.setIsSignedIn(it)
        }
    }

    override suspend fun exchangeCodeForAbroadToken(code: String) = withContext(ioDispatcher) {
        val response = authenticationRemoteDataSource.exchangeCodeForAboardToken(code)
        authenticationLocalDataSource.setAboardAccessToken((response.accessToken))
        authenticationLocalDataSource.setAboardTokenExpiration(response.expiresIn)
        authenticationLocalDataSource.setAboardTokenLastRefreshTime(
            System.currentTimeMillis().toInt()
        )
        authenticationLocalDataSource.setIsSignedIn(true)
    }

    override suspend fun refreshAboardToken() {
        val currentToken = authenticationLocalDataSource.getAboardAccessToken().first()
            ?: throw IllegalStateException("Aboard token is null")
        val response = authenticationRemoteDataSource.refreshAboardToken(currentToken)
        authenticationLocalDataSource.setAboardAccessToken(response.accessToken)
        authenticationLocalDataSource.setAboardTokenExpiration(response.expiresIn)
        authenticationLocalDataSource.setAboardTokenLastRefreshTime(
            System.currentTimeMillis().toInt()
        )
    }

    override suspend fun getAboardToken(): String? {
        return authenticationLocalDataSource.getAboardAccessToken().first()
    }

    override suspend fun clearAuthenticationData() = withContext(ioDispatcher) {
        authenticationLocalDataSource.clearAuthenticationData()
    }
}