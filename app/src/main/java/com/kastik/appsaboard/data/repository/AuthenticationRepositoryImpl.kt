package com.kastik.appsaboard.data.repository

import android.util.Log
import com.kastik.appsaboard.data.datasource.local.AuthenticationLocalDataSource
import com.kastik.appsaboard.data.datasource.remote.source.AuthenticationRemoteDataSource
import com.kastik.appsaboard.domain.models.AuthToken
import com.kastik.appsaboard.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val local: AuthenticationLocalDataSource,
    private val remote: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override suspend fun exchangeCodeForToken(code: String): AuthToken {
        val response = remote.exchangeCodeForToken(code)
        Log.d("MyLog", "ExchangeCodeForToken ${response.accessToken}")
        local.saveTokens(response.accessToken, response.refreshToken)
        return AuthToken(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            userId = response.userId
        )
    }

    override suspend fun getSavedToken(): String? {
        return local.getAccessToken()
    }

}
