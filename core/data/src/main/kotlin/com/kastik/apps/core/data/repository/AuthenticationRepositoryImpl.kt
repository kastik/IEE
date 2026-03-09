package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toLocalError
import com.kastik.apps.core.data.mappers.toNetworkError
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.model.result.Result
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
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

    override suspend fun refreshIsSignedIn() = withContext(ioDispatcher) {
        try {
            authenticationRemoteDataSource.checkIfTokenIsValid()
            Result.Success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.Error(e.toNetworkError())
        }
    }

    override suspend fun exchangeCodeForAbroadToken(code: String) =
        withContext(NonCancellable + ioDispatcher) {
            try {
                val response = authenticationRemoteDataSource.exchangeCodeForAboardToken(code)
                authenticationLocalDataSource.setAboardAccessToken((response.accessToken))
                authenticationLocalDataSource.setIsSignedIn(true)
                Result.Success(Unit)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                crashlytics.recordException(e)
                Result.Error(e.toNetworkError())
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
            Result.Error(e.toLocalError())
        }
    }
}