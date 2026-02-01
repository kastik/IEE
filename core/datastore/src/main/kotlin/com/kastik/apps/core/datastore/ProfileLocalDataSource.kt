package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscribedTagsProto
import com.kastik.apps.core.di.UserProfileDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProfileLocalDataSource {
    fun getProfile(): Flow<ProfileProto>
    fun getSubscriptions(): Flow<SubscribedTagsProto>
    suspend fun setProfile(userProfile: ProfileProto)
    suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>)
    suspend fun clearProfile()
    suspend fun clearSubscriptions()
}

internal class ProfileLocalDataSourceImpl @Inject constructor(
    @UserProfileDatastore private val profileDataStore: DataStore<ProfileProto>,
) : ProfileLocalDataSource {

    override fun getProfile(): Flow<ProfileProto> {
        return profileDataStore.data
    }

    override fun getSubscriptions(): Flow<SubscribedTagsProto> {
        return profileDataStore.data.map { it.subscribedTags }
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

    override suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        profileDataStore.updateData { currentProfile ->
            val tags = SubscribedTagsProto.newBuilder()
                .addAllSubscribedTags(userSubscriptions)
                .build()
            currentProfile.toBuilder()
                .setSubscribedTags(tags)
                .build()
        }
    }

    override suspend fun clearProfile() {
        profileDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }

    override suspend fun clearSubscriptions() {
        setSubscriptions(emptyList())
    }
}