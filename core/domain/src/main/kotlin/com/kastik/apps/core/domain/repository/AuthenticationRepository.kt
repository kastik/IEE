package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

//TODO Maybe we will need different auth errors for these
interface AuthenticationRepository {
    fun getIsSignedIn(): Flow<Boolean>
    suspend fun refreshIsSignedIn(): Result<Unit, AuthenticatedRefreshError>
    suspend fun refreshAboardToken(): Result<Unit, AuthenticatedRefreshError>
    suspend fun exchangeCodeForAbroadToken(code: String): Result<Unit, AuthenticatedRefreshError>
    suspend fun clearAuthenticationData(): Result<Unit, StorageError>

}