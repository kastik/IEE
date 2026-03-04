package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.error.LocalError
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>
    suspend fun refreshProfile(): Result<Unit, NetworkError>
    suspend fun clearLocalData(): Result<Unit, LocalError>
}