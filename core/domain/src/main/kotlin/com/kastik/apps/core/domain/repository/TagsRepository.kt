package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface TagsRepository {
    fun getAnnouncementTags(): Flow<List<Tag>>
    suspend fun refreshAnnouncementTags(): Result<Unit, NetworkError>
    fun getSubscribableTags(): Flow<List<SubscribableTag>>
    suspend fun refreshSubscribableTags(): Result<Unit, NetworkError>
    fun getSubscribedTags(): Flow<List<Tag>>
    suspend fun refreshSubscribedTags(): Result<Unit, NetworkError>
    suspend fun subscribeToTags(tagIds: List<Int>): Result<Unit, NetworkError>
}