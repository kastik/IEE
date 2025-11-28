package com.kastik.apps.core.network.datasource


import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.UpdateUserSubscriptionsDto
import com.kastik.apps.core.network.model.aboard.UserProfileDto
import com.kastik.apps.core.network.model.aboard.UserSubscribableTagsDto
import com.kastik.apps.core.network.model.aboard.UserSubscribedTagDto

interface UserInfoRemoteDataSource {
    suspend fun getUserProfile(): UserProfileDto

    suspend fun getUserSubscriptions(): List<UserSubscribedTagDto>

    suspend fun getUserSubscribableTags(): List<UserSubscribableTagsDto>

    suspend fun subscribeToTags(tags: List<Int>)

}

class UserInfoRemoteDataSourceImpl(
    @param:AnnRetrofit private val aboardApiClient: AboardApiClient,
) : UserInfoRemoteDataSource {

    override suspend fun getUserSubscribableTags(): List<UserSubscribableTagsDto> {
        return aboardApiClient.getUserSubscribableTags()
    }

    override suspend fun subscribeToTags(tags: List<Int>) {
        aboardApiClient.subscribeToTags(UpdateUserSubscriptionsDto(tags))
    }

    override suspend fun getUserProfile(): UserProfileDto {
        return aboardApiClient.getUserInfo()
    }

    override suspend fun getUserSubscriptions(): List<UserSubscribedTagDto> {
        return aboardApiClient.getUserSubscriptions()
    }

}