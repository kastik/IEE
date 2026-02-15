package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscribedTopicProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import com.kastik.apps.core.di.UserProfileDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProfileLocalDataSource {
    fun getProfile(): Flow<ProfileProto>
    fun getSubscriptions(): Flow<SubscriptionsProto>
    suspend fun setProfile(userProfile: ProfileProto)
    suspend fun setTagSubscriptions(userSubscriptions: List<SubscribedTagProto>)
    suspend fun setTopicSubscriptions(userSubscriptions: List<SubscribedTopicProto>)
    suspend fun clearProfile()
    suspend fun clearSubscriptions()
}

internal class ProfileLocalDataSourceImpl @Inject constructor(
    @UserProfileDatastore private val profileDataStore: DataStore<ProfileProto>,
) : ProfileLocalDataSource {

    override fun getProfile(): Flow<ProfileProto> {
        return profileDataStore.data
    }

    override fun getSubscriptions(): Flow<SubscriptionsProto> {
        return profileDataStore.data.map { it.subscriptions }
    }

    override suspend fun setProfile(userProfile: ProfileProto) {
        profileDataStore.updateData { currentProfile ->
            currentProfile.toBuilder()
                .setId(userProfile.id)
                .setName(userProfile.name)
                .setEmail(userProfile.email)
                .setCreatedAt(userProfile.createdAt)
                .setUpdatedAt(userProfile.updatedAt)
                .setIsAuthor(userProfile.isAuthor)
                .setIsAdmin(userProfile.isAdmin)
                .setLastLoginAt(userProfile.lastLoginAt)
                .setUid(userProfile.uid)
                .setDeletedAt(userProfile.deletedAt)
                .build()
        }
    }

    override suspend fun setTagSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        profileDataStore.updateData { currentProfile ->

            val existingSubsBuilder = currentProfile.subscriptions.toBuilder()

            val updatedSubs = existingSubsBuilder
                .clearSubscribedTags()
                .addAllSubscribedTags(userSubscriptions)
                .build()

            currentProfile.toBuilder()
                .setSubscriptions(updatedSubs)
                .build()
        }
    }

    override suspend fun setTopicSubscriptions(userSubscriptions: List<SubscribedTopicProto>) {
        profileDataStore.updateData { currentProfile ->

            val existingSubsBuilder = currentProfile.subscriptions.toBuilder()

            val updatedSubs = existingSubsBuilder
                .clearSubscribedTopics()
                .addAllSubscribedTopics(userSubscriptions)
                .build()

            currentProfile.toBuilder()
                .setSubscriptions(updatedSubs)
                .build()
        }
    }

    override suspend fun clearProfile() {
        profileDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }

    override suspend fun clearSubscriptions() {
        setTagSubscriptions(emptyList())
        setTopicSubscriptions(emptyList())
    }
}