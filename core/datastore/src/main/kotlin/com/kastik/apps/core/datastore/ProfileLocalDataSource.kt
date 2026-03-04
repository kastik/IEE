package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.di.UserProfileDatastore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProfileLocalDataSource {
    fun getProfile(): Flow<ProfileProto>
    suspend fun setProfile(userProfile: ProfileProto)
    suspend fun clearProfile()
}

internal class ProfileLocalDataSourceImpl @Inject constructor(
    @UserProfileDatastore private val profileDataStore: DataStore<ProfileProto>,
) : ProfileLocalDataSource {

    override fun getProfile(): Flow<ProfileProto> {
        return profileDataStore.data
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

    override suspend fun clearProfile() {
        profileDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }
}