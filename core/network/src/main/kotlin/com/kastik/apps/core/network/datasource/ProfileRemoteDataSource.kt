package com.kastik.apps.core.network.datasource


import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.profile.ProfileResponseDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribeToTagsRequestDto
import com.kastik.apps.core.network.model.aboard.tags.SubscribedTagResponseDto
import javax.inject.Inject

interface ProfileRemoteDataSource {
    suspend fun getProfile(): ProfileResponseDto
    suspend fun getEmailSubscriptions(): List<SubscribedTagResponseDto>
    suspend fun subscribeToEmailTags(tags: List<Int>)
}

internal class ProfileRemoteDataSourceImpl @Inject constructor(
    private val aboardApiClient: AboardApiClient,
) : ProfileRemoteDataSource {

    override suspend fun getProfile(): ProfileResponseDto {
        return aboardApiClient.getUserInfo()
    }

    override suspend fun getEmailSubscriptions(): List<SubscribedTagResponseDto> {
        return aboardApiClient.getUserSubscriptions()
    }

    override suspend fun subscribeToEmailTags(tags: List<Int>) {
        aboardApiClient.subscribeToTags(SubscribeToTagsRequestDto(tags))
    }

}