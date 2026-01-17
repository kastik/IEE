package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toSubscribableTag
import com.kastik.apps.core.data.mappers.toSubscribableTagProto
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.data.mappers.toTagEntity
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.model.aboard.SubscribableTag
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TagsRepositoryImpl @Inject constructor(
    private val announcementTagsLocalDataSource: TagsDao,
    private val subscribableTagsLocalDataSource: TagsLocalDataSource,
    private val tagsRemoteDataSource: TagsRemoteDataSource,
) : TagsRepository {

    override fun getAnnouncementTags(): Flow<List<Tag>> {
        return announcementTagsLocalDataSource.getTags().map { it.map { tag -> tag.toTag() } }
    }

    override suspend fun refreshAnnouncementTags() = withContext(Dispatchers.IO) {
        val remoteTags = tagsRemoteDataSource.fetchAnnouncementTags().data.map { it.toTagEntity() }
        announcementTagsLocalDataSource.insertOrReplaceTags(remoteTags)
    }

    override fun getSubscribableTags(): Flow<List<SubscribableTag>> {
        return subscribableTagsLocalDataSource.getSubscribableTags()
            .map { subscribableTags -> subscribableTags.tagsList.map { tag -> tag.toSubscribableTag() } }
    }

    override suspend fun refreshSubscribableTags() = withContext(Dispatchers.IO) {
        val subscribableTags = tagsRemoteDataSource.fetchSubscribableTags()
        subscribableTagsLocalDataSource.setSubscribableTags(subscribableTags.map { tag -> tag.toSubscribableTagProto() })
    }
}