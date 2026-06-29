package com.kastik.apps.core.datastore.datasource

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.di.UserProfileDatastore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProfileLocalDataSource {
    val profile: Flow<ProfileProto>
    suspend fun setProfile(userProfile: ProfileProto)
    suspend fun clearProfile()
}

internal class ProfileLocalDataSourceImpl @Inject constructor(
    @UserProfileDatastore private val profileDataStore: DataStore<ProfileProto>,
) : ProfileLocalDataSource {

    override val profile: Flow<ProfileProto> = profileDataStore.data


    override suspend fun setProfile(userProfile: ProfileProto) {
        profileDataStore.updateData { currentProfile ->
            currentProfile.toBuilder()
                .setId(userProfile.id)
                .setUid(userProfile.uid)
                .setName(userProfile.name)
                .setEmail(userProfile.email)
                .setIsAdmin(userProfile.isAdmin)
                .setIsAuthor(userProfile.isAuthor)
                .setCreatedAt(userProfile.createdAt)
                .setUpdatedAt(userProfile.updatedAt)
                .setDeletedAt(userProfile.deletedAt)
                .setLastLoginAt(userProfile.lastLoginAt)
                .build()
        }
    }

    override suspend fun clearProfile() {
        profileDataStore.updateData {
            it.toBuilder().clear().build()
        }
    }
}