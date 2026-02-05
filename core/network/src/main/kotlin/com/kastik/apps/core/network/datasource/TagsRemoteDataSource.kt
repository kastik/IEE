package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagsResponseDto

interface TagsRemoteDataSource {
    suspend fun fetchAnnouncementTags(): TagsResponseDto
    suspend fun fetchSubscribableTags(): List<SubscribableTagsResponseDto>
}

internal class TagsRemoteDataSourceImpl(
    @AnnRetrofit private val aboardApiClient: AboardApiClient
) : TagsRemoteDataSource {
    override suspend fun fetchAnnouncementTags(): TagsResponseDto =
        aboardApiClient.getTags()

    override suspend fun fetchSubscribableTags(): List<SubscribableTagsResponseDto> {
        return aboardApiClient.getUserSubscribableTags()
    }

}