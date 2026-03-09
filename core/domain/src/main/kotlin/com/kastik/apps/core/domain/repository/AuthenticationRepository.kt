package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.error.LocalError
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun getIsSignedIn(): Flow<Boolean>
    suspend fun refreshIsSignedIn(): Result<Unit, NetworkError>
    suspend fun exchangeCodeForAbroadToken(code: String): Result<Unit, NetworkError>
    suspend fun clearAuthenticationData(): Result<Unit, LocalError>

}