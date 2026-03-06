package com.kastik.apps.core.datastore.datasource

import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.datastore.proto.ProfileProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeProfileLocalDataSource : ProfileLocalDataSource {

    private var _profile = MutableStateFlow<ProfileProto>(ProfileProto.getDefaultInstance())
    private val profile = _profile.asStateFlow()
    override fun getProfile(): Flow<ProfileProto> = profile

    override suspend fun setProfile(userProfile: ProfileProto) {
        _profile.update {
            userProfile
        }
    }

    override suspend fun clearProfile() {
        _profile.update {
            ProfileProto.getDefaultInstance()
        }
    }
}