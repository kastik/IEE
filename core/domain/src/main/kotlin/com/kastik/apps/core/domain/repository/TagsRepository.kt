package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.Flow

interface TagsRepository {
    suspend fun getAnnouncementTags(): Flow<List<Tag>>
    fun getSubscribableTags(): Flow<List<SubscribableTag>>
    suspend fun refreshSubscribableTags()
}