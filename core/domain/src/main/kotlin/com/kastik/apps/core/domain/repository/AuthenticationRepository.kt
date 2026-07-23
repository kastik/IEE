package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val isSignedIn: Flow<Boolean>

    suspend fun signIn(code: String): Result<Unit, NetworkError>

    suspend fun signOut()
}
