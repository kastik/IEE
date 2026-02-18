package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagsResponseDto
import com.kastik.apps.core.network.testdata.subscribableTagsDtoTestData
import com.kastik.apps.core.network.testdata.tagsResponseDtoTestData

class FakeTagsRemoteDataSource : TagsRemoteDataSource {
    override suspend fun fetchAnnouncementTags(): TagsResponseDto {
        return tagsResponseDtoTestData
    }

    override suspend fun fetchSubscribableTags(): List<SubscribableTagsResponseDto> {
        return subscribableTagsDtoTestData
    }
}