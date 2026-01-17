package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toProfile
import com.kastik.apps.core.data.mappers.toProfileProto
import com.kastik.apps.core.data.mappers.toSubscribedTagProto
import com.kastik.apps.core.data.mappers.toTag
import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.aboard.Profile
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import com.kastik.apps.core.notifications.PushNotificationsDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ProfileRepositoryImpl @Inject constructor(
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val pushNotificationsDatasource: PushNotificationsDatasource,
) : ProfileRepository {

    override fun isSignedIn(): Flow<Boolean> {
        return profileLocalDataSource.getProfile().map { it.id != 0 }
    }

    override fun getProfile(): Flow<Profile> {
        return profileLocalDataSource.getProfile().map { profile -> profile.toProfile() }
    }

    override suspend fun refreshProfile() = withContext(Dispatchers.IO) {
        val userProfile = profileRemoteDataSource.getProfile()
        profileLocalDataSource.setProfile(userProfile.toProfileProto())
    }

    override fun getEmailSubscriptions(): Flow<List<Tag>> {
        return profileLocalDataSource.getSubscriptions()
            .map { tagList -> tagList.subscribedTagsList.map { it.toTag() } }
    }

    override suspend fun refreshEmailSubscriptions() = withContext(Dispatchers.IO) {
        val subscribedTags = profileRemoteDataSource.getEmailSubscriptions()
        profileLocalDataSource.setSubscriptions(subscribedTags.map { tag -> tag.toSubscribedTagProto() })
    }

    override suspend fun subscribeToEmailTags(tagIds: List<Int>) = withContext(Dispatchers.IO) {
        profileRemoteDataSource.subscribeToEmailTags(tagIds)
    }

    override suspend fun subscribeToTopics(tagIds: List<Int>) = withContext(Dispatchers.IO) {
        pushNotificationsDatasource.subscribeToPushTags(tagIds)
    }

    override suspend fun unsubscribeFromAllTopics() = withContext(Dispatchers.IO) {
        pushNotificationsDatasource.unSubscribeFromPushTags()
    }

    override suspend fun clearLocalData() = withContext(Dispatchers.IO) {
        profileLocalDataSource.clearProfile()
        profileLocalDataSource.clearSubscriptions()
    }
}