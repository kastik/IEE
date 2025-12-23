package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.Flow

interface TagsRepository {
    fun getAnnouncementTags(): Flow<List<Tag>>
    suspend fun refreshAnnouncementTags()
    fun getSubscribableTags(): Flow<List<SubscribableTag>>
    suspend fun refreshSubscribableTags()
}