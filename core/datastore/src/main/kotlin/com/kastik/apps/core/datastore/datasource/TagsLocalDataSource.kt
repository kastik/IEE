package com.kastik.apps.core.datastore.datasource

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.di.UserSubscribableTagsDatastore
import com.kastik.apps.core.datastore.di.UserSubscriptionsDatastore
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import com.kastik.apps.core.datastore.serializers.SubscribableTagsSerializer
import com.kastik.apps.core.datastore.serializers.SubscribedTagsSerializer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface TagsLocalDataSource {
    val subscriptions: Flow<SubscriptionsProto>
    val subscribableTags: Flow<SubscribableTagsProto>

    suspend fun setSubscribableTags(tags: List<SubscribableTagProto>)

    suspend fun clearSubscribableTags()

    suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>)

    suspend fun clearSubscriptions()
}

@Singleton
internal class TagsLocalDataSourceImpl
@Inject
constructor(
    @UserSubscriptionsDatastore private val subscribedTagsDatastore: DataStore<SubscriptionsProto>,
    @UserSubscribableTagsDatastore
    private val subscribableTagsDatastore: DataStore<SubscribableTagsProto>,
) : TagsLocalDataSource {

    override val subscriptions = subscribedTagsDatastore.data

    override val subscribableTags = subscribableTagsDatastore.data

    override suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        subscribedTagsDatastore.updateData { subscribedTags ->
            subscribedTags.toBuilder().clearTags().addAllTags(userSubscriptions).build()
        }
    }

    override suspend fun setSubscribableTags(tags: List<SubscribableTagProto>) {
        subscribableTagsDatastore.updateData { subscribableTags ->
            subscribableTags.toBuilder().clearTags().addAllTags(tags).build()
        }
    }

    override suspend fun clearSubscribableTags() {
        subscribableTagsDatastore.updateData { subscribableTags ->
            SubscribableTagsSerializer.defaultValue
        }
    }

    override suspend fun clearSubscriptions() {
        subscribedTagsDatastore.updateData { subscribedTags ->
            SubscribedTagsSerializer.defaultValue
        }
    }
}
