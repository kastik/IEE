package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.domain.PrivateRefreshError
import com.kastik.apps.core.domain.Result
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>
    suspend fun refreshProfile(): Result<Unit, PrivateRefreshError>
    fun getEmailSubscriptions(): Flow<List<Tag>>
    suspend fun refreshEmailSubscriptions(): Result<Unit, PrivateRefreshError>
    suspend fun subscribeToEmailTags(tagIds: List<Int>): Result<Unit, PrivateRefreshError>
    suspend fun subscribeToTopics(tagIds: List<Int>)
    suspend fun unsubscribeFromTopics(tagIds: List<Int>)
    suspend fun unsubscribeFromAllTopics()
    suspend fun clearLocalData()
}