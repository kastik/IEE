package com.kastik.apps.core.datastore.datasource

import com.kastik.apps.core.datastore.proto.ProfileProto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeProfileLocalDataSource : ProfileLocalDataSource {

    private var _profile = MutableStateFlow<ProfileProto?>(null)

    override val profile = _profile.asStateFlow()

    override suspend fun setProfile(profile: ProfileProto) {
        _profile.update {
            if (profile == ProfileProto.getDefaultInstance()) null else profile
        }
    }

    override suspend fun clearProfile() {
        _profile.update {
            null
        }
    }
}
