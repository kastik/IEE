package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>
    suspend fun refreshProfile(): Result<Unit, AuthenticatedRefreshError>
    fun getEmailSubscriptions(): Flow<List<Tag>>
    fun getTopicSubscriptions(): Flow<List<Int>>
    suspend fun refreshEmailSubscriptions(): Result<Unit, AuthenticatedRefreshError>
    suspend fun subscribeToEmailTags(tagIds: List<Int>): Result<Unit, AuthenticatedRefreshError>
    suspend fun syncTopicSubscriptions(): Result<Unit, GeneralRefreshError>
    suspend fun unsubscribeFromAllTopics()
    suspend fun clearLocalData(): Result<Unit, StorageError>
}