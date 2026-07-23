package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.data.mappers.toNetworkError
import com.kastik.apps.core.data.utils.safeCall
import com.kastik.apps.core.datastore.datasource.AuthenticationLocalDataSource
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
internal class AuthenticationRepositoryImpl
@Inject
constructor(
    private val crashlytics: Crashlytics,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthenticationRepository {

    override val isSignedIn: Flow<Boolean> = authenticationLocalDataSource.isSignedIn

    override suspend fun signIn(code: String) =
        withContext(NonCancellable + ioDispatcher) {
            safeCall(
                mapException = Exception::toNetworkError,
                recordException = crashlytics::recordException,
            ) {
                val response = authenticationRemoteDataSource.exchangeCodeForAboardToken(code)
                authenticationLocalDataSource.setAboardAccessToken((response.accessToken))
                authenticationLocalDataSource.setIsSignedIn(true)
            }
        }

    override suspend fun signOut() =
        withContext(NonCancellable + ioDispatcher) {
            authenticationLocalDataSource.clearAuthenticationData()
        }
}
