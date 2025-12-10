package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.SubscribableTagsDto
import com.kastik.apps.core.network.model.aboard.TagsResponse

interface TagsRemoteDataSource {
    suspend fun fetchTags(): TagsResponse
    suspend fun getSubscribableTags(): List<SubscribableTagsDto>
}

internal class TagsRemoteDataSourceImpl(
    @param:AnnRetrofit private val aboardApiClient: AboardApiClient
) : TagsRemoteDataSource {
    override suspend fun fetchTags(): TagsResponse =
        aboardApiClient.getTags()

    override suspend fun getSubscribableTags(): List<SubscribableTagsDto> {
        return aboardApiClient.getUserSubscribableTags()
    }

}