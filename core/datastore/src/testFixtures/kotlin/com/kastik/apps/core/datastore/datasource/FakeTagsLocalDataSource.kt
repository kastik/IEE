package com.kastik.apps.core.datastore.datasource

import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeTagsLocalDataSource() : TagsLocalDataSource {

    private val _subscribableTags =
        MutableStateFlow<SubscribableTagsProto>(SubscribableTagsProto.getDefaultInstance())
    private var _subscriptions =
        MutableStateFlow<SubscriptionsProto>(SubscriptionsProto.getDefaultInstance())

    override val subscriptions = _subscriptions.asStateFlow()

    override val subscribableTags = _subscribableTags.asStateFlow()

    override suspend fun setSubscribableTags(tags: List<SubscribableTagProto>) {
        _subscribableTags.update {
            it.toBuilder().clearTags().addAllTags(tags).build()
        }
    }

    override suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        _subscriptions.update {
            it.toBuilder().clearTags().addAllTags(userSubscriptions).build()
        }
    }

    override suspend fun clearSubscriptions() {
        _subscriptions.update {
            it.toBuilder().clearTags().build()
        }
    }

    override suspend fun clearSubscribableTags() {
        _subscribableTags.update {
            it.toBuilder().clearTags().build()
        }
    }
}
