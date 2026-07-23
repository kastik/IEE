package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface TagsRepository {
    val announcementTags: Flow<List<Tag>>
    val subscribedTags: Flow<List<Tag>>
    val subscribableTags: Flow<List<Tag>>

    suspend fun syncAnnouncementTags(): Result<Unit, NetworkError>

    suspend fun syncSubscribedTags(): Result<Unit, NetworkError>

    suspend fun syncSubscribableTags(): Result<Unit, NetworkError>

    suspend fun subscribeToTags(tagIds: List<Int>): Result<Unit, NetworkError>
}
