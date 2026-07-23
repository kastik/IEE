package com.kastik.apps.core.datastore.datasource

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.di.UserProfileDatastore
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.serializers.ProfileSerializer
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProfileLocalDataSource {
    val profile: Flow<ProfileProto?>

    suspend fun setProfile(profile: ProfileProto)

    suspend fun clearProfile()
}

@Singleton
internal class ProfileLocalDataSourceImpl
@Inject
constructor(@UserProfileDatastore private val profileDataStore: DataStore<ProfileProto>) :
    ProfileLocalDataSource {

    override val profile: Flow<ProfileProto?> =
        profileDataStore.data.map { profile ->
            profile.takeUnless { it == ProfileSerializer.defaultValue }
        }

    override suspend fun setProfile(profile: ProfileProto) {
        profileDataStore.updateData { currentProfile ->
            profile
        }
    }

    override suspend fun clearProfile() {
        profileDataStore.updateData { currentProfile ->
            ProfileSerializer.defaultValue
        }
    }
}
