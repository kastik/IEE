package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.tags.SubscribableTagsResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribeToTagsRequestDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import com.kastik.apps.core.network.model.aboard.tags.TagsResponseDto
import javax.inject.Inject

interface TagsRemoteDataSource {
    suspend fun fetchAnnouncementTags(): TagsResponseDto
    suspend fun fetchSubscribableTags(): List<SubscribableTagsResponseDto>
    suspend fun fetchSubscriptions(): List<SubscribedTagResponseDto>
    suspend fun subscribeToTags(tagIds: List<Int>)
}

internal class TagsRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient
) : TagsRemoteDataSource {
    override suspend fun fetchAnnouncementTags(): TagsResponseDto =
        aboardApiClient.getTags()

    override suspend fun fetchSubscribableTags(): List<SubscribableTagsResponseDto> {
        return aboardApiClient.getUserSubscribableTags()
    }

    override suspend fun fetchSubscriptions(): List<SubscribedTagResponseDto> {
        return aboardApiClient.getUserSubscriptions()
    }

    override suspend fun subscribeToTags(tagIds: List<Int>) {
        aboardApiClient.subscribeToTags(SubscribeToTagsRequestDto(tagIds))
    }

}