package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toDomain
import com.kastik.apps.core.domain.repository.UserInfoRepository
import com.kastik.apps.core.model.aboard.UserProfile
import com.kastik.apps.core.model.aboard.UserSubscribableTag
import com.kastik.apps.core.model.aboard.UserSubscribedTag
import com.kastik.apps.core.network.datasource.UserInfoRemoteDataSource

class UserInfoRepositoryImpl(
    private val remoteDataSource: UserInfoRemoteDataSource,
) : UserInfoRepository {
    override suspend fun getUserProfile(): UserProfile {
        return remoteDataSource.getUserProfile().toDomain()
    }

    override suspend fun getUserSubscriptions(): List<UserSubscribedTag> {
        return remoteDataSource.getUserSubscriptions().map { it.toDomain() }
    }


    override suspend fun getUserSubscribableTags(): List<UserSubscribableTag> {
        return remoteDataSource.getUserSubscribableTags().map { it.toDomain() }
    }

    override suspend fun subscribeToTags(tags: List<Int>) {
        remoteDataSource.subscribeToTags(tags)
    }

}