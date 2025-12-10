package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toSubscribableTag
import com.kastik.apps.core.data.mappers.toSubscribableTagProto
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class TagsRepositoryImpl(
    private val tagsLocalDataSource: TagsLocalDataSource,
    private val tagsRemoteDataSource: TagsRemoteDataSource,
) : TagsRepository {

    override suspend fun getAnnouncementTags(): Flow<List<Tag>> {
        return flowOf(tagsRemoteDataSource.fetchTags().data.map { it.toTag() })
    }

    override fun getSubscribableTags(): Flow<List<SubscribableTag>> {
        return tagsLocalDataSource.getSubscribableTags()
            .map { subscribableTags -> subscribableTags.tagsList.map { tag -> tag.toSubscribableTag() } }
    }

    override suspend fun refreshSubscribableTags() {
        val subscribableTags = tagsRemoteDataSource.getSubscribableTags()
        tagsLocalDataSource.setSubscribableTags(subscribableTags.map { tag -> tag.toSubscribableTagProto() })
    }
}