package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.domain.PrivateRefreshError
import com.kastik.apps.core.domain.Result
import kotlinx.coroutines.flow.Flow

//TODO Maybe we will need different auth errors for these
interface AuthenticationRepository {
    fun getIsSignedIn(): Flow<Boolean>
    suspend fun refreshIsSignedIn(): Result<Unit, PrivateRefreshError>
    suspend fun refreshAboardToken(): Result<Unit, PrivateRefreshError>
    suspend fun exchangeCodeForAbroadToken(code: String): Result<Unit, PrivateRefreshError>
    suspend fun clearAuthenticationData()

}