package com.kastik.apps.core.datastore.datasource

import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeTagsLocalDataSource : TagsLocalDataSource {

    private val _subscribableTags = MutableStateFlow<List<SubscribableTagProto>>(emptyList())

    override fun getSubscribableTags(): Flow<SubscribableTagsProto> {
        return _subscribableTags.map { list ->
            SubscribableTagsProto.newBuilder()
                .addAllTags(list)
                .build()
        }
    }

    override suspend fun setSubscribableTags(tags: List<SubscribableTagProto>) {
        _subscribableTags.update {
            tags
        }
    }

    override suspend fun clearSubscribableTags() {
        _subscribableTags.update { emptyList() }
    }
}