package com.kastik.apps.core.network.datasource


import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.SubscribedTagDto
import com.kastik.apps.core.network.model.aboard.UpdateUserSubscriptionsDto
import com.kastik.apps.core.network.model.aboard.UserProfileDto

interface ProfileRemoteDataSource {
    suspend fun getProfile(): UserProfileDto
    suspend fun getEmailSubscriptions(): List<SubscribedTagDto>
    suspend fun subscribeToEmailTags(tags: List<Int>)
}

internal class ProfileRemoteDataSourceImpl(
    @param:AnnRetrofit private val aboardApiClient: AboardApiClient,
) : ProfileRemoteDataSource {

    override suspend fun getProfile(): UserProfileDto {
        return aboardApiClient.getUserInfo()
    }

    override suspend fun getEmailSubscriptions(): List<SubscribedTagDto> {
        return aboardApiClient.getUserSubscriptions()
    }

    override suspend fun subscribeToEmailTags(tags: List<Int>) {
        aboardApiClient.subscribeToTags(UpdateUserSubscriptionsDto(tags))
    }

}