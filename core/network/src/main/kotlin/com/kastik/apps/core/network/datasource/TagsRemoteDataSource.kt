package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.model.request.SubscribeDto
import com.kastik.apps.core.network.model.response.TagResponseDto
import com.kastik.apps.core.network.model.response.AnnouncementTagsResponseDto
import javax.inject.Inject

interface TagsRemoteDataSource {
    suspend fun fetchAnnouncementTags(): AnnouncementTagsResponseDto
    suspend fun fetchSubscribableTags(): List<TagResponseDto>
    suspend fun fetchSubscriptions(): List<TagResponseDto>
    suspend fun subscribeToTags(tagIds: List<Int>)
}

internal class TagsRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient
) : TagsRemoteDataSource {
    override suspend fun fetchAnnouncementTags(): AnnouncementTagsResponseDto =
        aboardApiClient.getTags()

    override suspend fun fetchSubscribableTags(): List<TagResponseDto> {
        return aboardApiClient.getAvailableTags()
    }

    override suspend fun fetchSubscriptions(): List<TagResponseDto> {
        return aboardApiClient.getSubscribedTags()
    }

    override suspend fun subscribeToTags(tagIds: List<Int>) {
        aboardApiClient.subscribeToTags(SubscribeDto(tagIds))
    }

}