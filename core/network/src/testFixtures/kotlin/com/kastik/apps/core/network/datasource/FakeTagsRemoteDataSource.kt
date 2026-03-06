package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagsResponseDto
import com.kastik.apps.core.network.testdata.subscribableTagsDtoTestData
import com.kastik.apps.core.network.testdata.subscribedTagDtoTestData
import com.kastik.apps.core.network.testdata.tagsResponseDtoTestData

class FakeTagsRemoteDataSource : TagsRemoteDataSource {
    private val _subscribableTags = mutableListOf<Int>()

    override suspend fun fetchAnnouncementTags(): TagsResponseDto {
        return tagsResponseDtoTestData
    }

    override suspend fun fetchSubscribableTags(): List<SubscribableTagsResponseDto> {
        return subscribableTagsDtoTestData
    }

    override suspend fun fetchSubscriptions(): List<SubscribedTagResponseDto> {
        return subscribedTagDtoTestData
    }

    override suspend fun subscribeToTags(tagIds: List<Int>) {
        _subscribableTags.clear()
        _subscribableTags.addAll(tagIds)
    }

}