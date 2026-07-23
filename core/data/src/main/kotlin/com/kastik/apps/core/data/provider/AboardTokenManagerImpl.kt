package com.kastik.apps.core.data.provider

import com.kastik.apps.core.datastore.datasource.AuthenticationLocalDataSource
import com.kastik.apps.core.network.interceptor.TokenManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class AboardTokenManagerImpl
@Inject
constructor(val authenticationLocalDataSource: AuthenticationLocalDataSource) : TokenManager {

    override suspend fun getToken(): String? =
        authenticationLocalDataSource.aboardAccessToken.first()

    override suspend fun updateToken(newToken: String) =
        authenticationLocalDataSource.setAboardAccessToken(newToken)

    override suspend fun tokenExpired() {
        authenticationLocalDataSource.clearAuthenticationData()
    }
}
