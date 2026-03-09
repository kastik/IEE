package com.kastik.apps.core.datastore


import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import com.kastik.apps.core.di.UserSubscribableTagsDatastore
import com.kastik.apps.core.di.UserSubscriptionsDatastore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TagsLocalDataSource {
    fun getSubscribableTags(): Flow<SubscribableTagsProto>
    suspend fun setSubscribableTags(tags: List<SubscribableTagProto>)
    suspend fun clearSubscribableTags()
    fun getSubscriptions(): Flow<SubscriptionsProto>
    suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>)
    suspend fun clearSubscriptions()
}

internal class TagsLocalDataSourceImpl @Inject constructor(
    @UserSubscriptionsDatastore private val subscribedTagsDatastore: DataStore<SubscriptionsProto>,
    @UserSubscribableTagsDatastore private val subscribableTagsDatastore: DataStore<SubscribableTagsProto>,
) : TagsLocalDataSource {

    override fun getSubscribableTags(): Flow<SubscribableTagsProto> {
        return subscribableTagsDatastore.data
    }

    override suspend fun setSubscribableTags(tags: List<SubscribableTagProto>) {
        subscribableTagsDatastore.updateData { subscribableTags ->
            subscribableTags.toBuilder()
                .clearTags()
                .addAllTags(tags)
                .build()
        }
    }

    override suspend fun clearSubscribableTags() {
        subscribableTagsDatastore.updateData { subscribableTags ->
            subscribableTags.toBuilder()
                .clearTags()
                .build()
        }
    }


    override fun getSubscriptions(): Flow<SubscriptionsProto> {
        return subscribedTagsDatastore.data
    }

    override suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        subscribedTagsDatastore.updateData { subscribedTags ->

            subscribedTags
                .toBuilder()
                .clearSubscribedTags()
                .addAllSubscribedTags(userSubscriptions)
                .build()
        }
    }

    override suspend fun clearSubscriptions() {
        subscribedTagsDatastore.updateData { subscribedTags ->

            subscribedTags
                .toBuilder()
                .clearSubscribedTags()
                .build()
        }
    }
}