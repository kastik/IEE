package com.kastik.apps.core.datastore


import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.di.UserSubscribableTagsDatastore
import kotlinx.coroutines.flow.Flow

interface TagsLocalDataSource {
    fun getSubscribableTags(): Flow<SubscribableTagsProto>
    suspend fun setSubscribableTags(tags: List<SubscribableTagProto>)
    suspend fun clearSubscribableTags()
}

internal class TagsLocalDataSourceImpl(
    @param:UserSubscribableTagsDatastore private val dataStore: DataStore<SubscribableTagsProto>
) : TagsLocalDataSource {

    override fun getSubscribableTags(): Flow<SubscribableTagsProto> {
        return dataStore.data
    }

    override suspend fun setSubscribableTags(tags: List<SubscribableTagProto>) {
        dataStore.updateData { current ->
            current.toBuilder()
                .clearTags()
                .addAllTags(tags)
                .build()
        }
    }

    override suspend fun clearSubscribableTags() {
        setSubscribableTags(emptyList())
    }
}