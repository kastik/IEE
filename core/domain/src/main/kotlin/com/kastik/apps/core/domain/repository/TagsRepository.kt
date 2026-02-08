package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.model.error.GeneralRefreshError
import com.kastik.apps.core.model.result.Result
import kotlinx.coroutines.flow.Flow

interface TagsRepository {
    fun getAnnouncementTags(): Flow<List<Tag>>
    suspend fun refreshAnnouncementTags(): Result<Unit, GeneralRefreshError>
    fun getSubscribableTags(): Flow<List<SubscribableTag>>
    suspend fun refreshSubscribableTags(): Result<Unit, GeneralRefreshError>
}