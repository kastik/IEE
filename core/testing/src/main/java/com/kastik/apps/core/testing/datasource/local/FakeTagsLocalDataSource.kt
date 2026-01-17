package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.datastore.proto.SubscribableTagProto
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import com.kastik.apps.core.testing.testdata.subscribableTagsProtoTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class FakeTagsLocalDataSource : TagsLocalDataSource {

    private val _subscribableTags = MutableStateFlow<List<SubscribableTagProto>>(emptyList())

    override fun getSubscribableTags(): Flow<SubscribableTagsProto> {
        return flowOf(subscribableTagsProtoTestData)
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