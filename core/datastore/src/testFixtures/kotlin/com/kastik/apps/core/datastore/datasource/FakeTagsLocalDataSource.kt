package com.kastik.apps.core.datastore.datasource

import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeTagsLocalDataSource : TagsLocalDataSource {

    private val _subscribableTags =
        MutableStateFlow<SubscribableTagsProto>(SubscribableTagsProto.getDefaultInstance())
    private var _subscriptions =
        MutableStateFlow<SubscriptionsProto>(SubscriptionsProto.getDefaultInstance())

    override fun getSubscribableTags(): Flow<SubscribableTagsProto> = _subscribableTags

    override suspend fun setSubscribableTags(tags: List<SubscribableTagProto>) {
        _subscribableTags.update {
            it.toBuilder().clearTags().addAllTags(tags).build()
        }
    }

    override suspend fun clearSubscribableTags() {
        _subscribableTags.update {
            it.toBuilder().clearTags().build()
        }
    }

    override fun getSubscriptions(): Flow<SubscriptionsProto> = _subscriptions


    override suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        _subscriptions.update {
            it.toBuilder().clearSubscribedTags().addAllSubscribedTags(userSubscriptions).build()
        }
    }

    override suspend fun clearSubscriptions() {
        _subscriptions.update {
            it.toBuilder().clearSubscribedTags().build()
        }
    }
}