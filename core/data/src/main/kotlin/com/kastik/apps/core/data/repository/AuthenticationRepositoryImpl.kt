package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.data.mappers.toPrivateRefreshError
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.Result
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

    override suspend fun refreshIsSignedIn() =
        try {
            authenticationRemoteDataSource.checkIfTokenIsValid().let {
                authenticationLocalDataSource.setIsSignedIn(it)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toPrivateRefreshError())
        }


    override suspend fun exchangeCodeForAbroadToken(code: String) = withContext(ioDispatcher) {
        try {
            val response = authenticationRemoteDataSource.exchangeCodeForAboardToken(code)
            authenticationLocalDataSource.setAboardAccessToken((response.accessToken))
            authenticationLocalDataSource.setAboardTokenExpiration(response.expiresIn)
            authenticationLocalDataSource.setAboardTokenLastRefreshTime(System.currentTimeMillis())
            authenticationLocalDataSource.setIsSignedIn(true)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toPrivateRefreshError())
        }

    }

    override suspend fun refreshAboardToken() =
        try {
            val currentToken = authenticationLocalDataSource.getAboardAccessToken().first()
                ?: throw IllegalStateException("Aboard token is null")
            val response = authenticationRemoteDataSource.refreshAboardToken(currentToken)
            authenticationLocalDataSource.setAboardAccessToken(response.accessToken)
            authenticationLocalDataSource.setAboardTokenExpiration(response.expiresIn)
            authenticationLocalDataSource.setAboardTokenLastRefreshTime(System.currentTimeMillis())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toPrivateRefreshError())
        }


    override suspend fun clearAuthenticationData() = withContext(ioDispatcher) {
        authenticationLocalDataSource.clearAuthenticationData()
    }
}