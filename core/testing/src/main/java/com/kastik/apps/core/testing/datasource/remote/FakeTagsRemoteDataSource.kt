package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import com.kastik.apps.core.network.model.aboard.SubscribableTagsDto
import com.kastik.apps.core.network.model.aboard.TagsResponseDto
import com.kastik.apps.core.testing.testdata.subscribableTagsDtoTestData
import com.kastik.apps.core.testing.testdata.tagsResponseDtoTestData

class FakeTagsRemoteDataSource : TagsRemoteDataSource {
    override suspend fun fetchAnnouncementTags(): TagsResponseDto {
        return tagsResponseDtoTestData
    }

    override suspend fun fetchSubscribableTags(): List<SubscribableTagsDto> {
        return subscribableTagsDtoTestData
    }
}