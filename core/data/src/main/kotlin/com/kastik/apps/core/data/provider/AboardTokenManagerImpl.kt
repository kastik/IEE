package com.kastik.apps.core.data.provider

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.network.interceptor.TokenManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AboardTokenManagerImpl @Inject constructor(
    val authenticationLocalDataSource: AuthenticationLocalDataSource,
) : TokenManager {

    override suspend fun getToken(): String? =
        authenticationLocalDataSource.getAboardAccessToken().first()

    override suspend fun updateToken(newToken: String) =
        authenticationLocalDataSource.setAboardAccessToken(newToken)

}