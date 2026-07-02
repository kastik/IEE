package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.di.AuthenticatorAboardClient
import com.kastik.apps.core.network.model.common.ListResponseDto
import com.kastik.apps.core.network.model.request.SubscribeDto
import com.kastik.apps.core.network.model.response.TagDto
import javax.inject.Inject

interface TagsRemoteDataSource {
    suspend fun fetchAnnouncementTags(): ListResponseDto<TagDto>
    suspend fun fetchSubscribableTags(): List<TagDto>
    suspend fun fetchSubscriptions(): List<TagDto>
    suspend fun subscribeToTags(tagIds: List<Int>)
}

internal class TagsRemoteDataSourceImpl @Inject constructor(
    @AuthenticatorAboardClient private val aboardApiClient: AboardApiClient
) : TagsRemoteDataSource {

    override suspend fun fetchAnnouncementTags() =
        aboardApiClient.getTags()

    override suspend fun fetchSubscribableTags() =
        aboardApiClient.getAvailableTags()

    override suspend fun fetchSubscriptions() =
        aboardApiClient.getSubscribedTags()

    override suspend fun subscribeToTags(tagIds: List<Int>) =
        aboardApiClient.subscribeToTags(SubscribeDto(tagIds))

}