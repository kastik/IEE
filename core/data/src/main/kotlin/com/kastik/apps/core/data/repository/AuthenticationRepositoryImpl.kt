package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toPrivateRefreshError
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
internal class AuthenticationRepositoryImpl @Inject constructor(
    private val crashlytics: Crashlytics,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthenticationRepository {

    override fun getIsSignedIn(): Flow<Boolean> =
        authenticationLocalDataSource.getIsSignedIn()

    override suspend fun refreshIsSignedIn(): Result<Unit, AuthenticatedRefreshError> =
        try {
            val hasToken = authenticationLocalDataSource.getAboardAccessToken().first() != null
            if (!hasToken) {
                authenticationLocalDataSource.setIsSignedIn(false)
                return Result.Success(Unit)
            }

            val isAuthenticated = authenticationRemoteDataSource.checkIfTokenIsValid()
            authenticationLocalDataSource.setIsSignedIn(isAuthenticated)

            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toPrivateRefreshError())
        }

    override suspend fun exchangeCodeForAbroadToken(code: String) =
        withContext(NonCancellable + ioDispatcher) {
        try {
            val response = authenticationRemoteDataSource.exchangeCodeForAboardToken(code)
            authenticationLocalDataSource.setAboardAccessToken((response.accessToken))
            authenticationLocalDataSource.setAboardTokenExpiration(response.expiresIn)
            authenticationLocalDataSource.setAboardTokenLastRefreshTime(System.currentTimeMillis())
            authenticationLocalDataSource.setIsSignedIn(true)
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toPrivateRefreshError())
        }

    }

    override suspend fun refreshAboardToken() = withContext(NonCancellable + ioDispatcher) {
        try {
            val currentToken = authenticationLocalDataSource.getAboardAccessToken().first()
                ?: throw IllegalStateException("Aboard token is null")
            val response = authenticationRemoteDataSource.refreshAboardToken(currentToken)
            authenticationLocalDataSource.setAboardAccessToken(response.accessToken)
            authenticationLocalDataSource.setAboardTokenExpiration(response.expiresIn)
            authenticationLocalDataSource.setAboardTokenLastRefreshTime(System.currentTimeMillis())
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toPrivateRefreshError())
        }
    }


    override suspend fun clearAuthenticationData() = withContext(NonCancellable + ioDispatcher) {
        try {
            authenticationLocalDataSource.clearAuthenticationData()
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(StorageError)
        }
    }
}