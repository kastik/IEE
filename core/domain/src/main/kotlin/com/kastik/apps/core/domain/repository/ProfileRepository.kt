package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.AuthenticatedRefreshError
import com.kastik.apps.core.model.error.StorageError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>
    suspend fun refreshProfile(): Result<Unit, AuthenticatedRefreshError>
    fun getEmailSubscriptions(): Flow<List<Tag>>
    suspend fun refreshEmailSubscriptions(): Result<Unit, AuthenticatedRefreshError>
    suspend fun subscribeToEmailTags(tagIds: List<Int>): Result<Unit, AuthenticatedRefreshError>
    suspend fun subscribeToTopics(tagIds: List<Int>) //TODO, ADD RESULT IN THESE TOO!
    suspend fun unsubscribeFromTopics(tagIds: List<Int>)
    suspend fun unsubscribeFromAllTopics()
    suspend fun clearLocalData(): Result<Unit, StorageError>
}