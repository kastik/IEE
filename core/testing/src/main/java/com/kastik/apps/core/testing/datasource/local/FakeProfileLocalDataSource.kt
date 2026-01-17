package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscribedTagsProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeProfileLocalDataSource : ProfileLocalDataSource {

    private var profile = ProfileProto.getDefaultInstance()
    private var subscriptions = SubscribedTagsProto.getDefaultInstance()

    override fun getProfile(): Flow<ProfileProto> = flowOf(profile)

    override fun getSubscriptions(): Flow<SubscribedTagsProto> = flowOf(subscriptions)

    override suspend fun setProfile(userProfile: ProfileProto) {
        profile = userProfile
    }

    override suspend fun setSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        subscriptions = SubscribedTagsProto.newBuilder()
            .addAllSubscribedTags(userSubscriptions)
            .build()
    }

    override suspend fun clearProfile() {
        profile = ProfileProto.getDefaultInstance()
    }

    override suspend fun clearSubscriptions() {
        subscriptions = SubscribedTagsProto.getDefaultInstance()
    }
}