package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun isSignedIn(): Flow<Boolean>
    fun getProfile(): Flow<Profile>
    suspend fun refreshProfile()
    fun getEmailSubscriptions(): Flow<List<Tag>>
    suspend fun refreshEmailSubscriptions()
    suspend fun subscribeToEmailTags(tagIds: List<Int>)
    suspend fun subscribeToTopics(tagIds: List<Int>)
    suspend fun unsubscribeFromAllTopics()
    suspend fun clearLocalData()
}