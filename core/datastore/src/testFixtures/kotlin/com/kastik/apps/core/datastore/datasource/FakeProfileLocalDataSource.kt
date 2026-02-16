package com.kastik.apps.core.datastore.datasource

import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.datastore.proto.ProfileProto
import com.kastik.apps.core.datastore.proto.SubscribedTagProto
import com.kastik.apps.core.datastore.proto.SubscribedTopicProto
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeProfileLocalDataSource : ProfileLocalDataSource {

    private var profile = ProfileProto.getDefaultInstance()
    private var subscriptions = SubscriptionsProto.getDefaultInstance()

    override fun getProfile(): Flow<ProfileProto> = flowOf(profile)

    override fun getSubscriptions(): Flow<SubscriptionsProto> = flowOf(subscriptions)

    override suspend fun setProfile(userProfile: ProfileProto) {
        profile = userProfile
    }

    override suspend fun setTagSubscriptions(userSubscriptions: List<SubscribedTagProto>) {
        subscriptions = SubscriptionsProto.newBuilder()
            .clearSubscribedTags()
            .addAllSubscribedTags(userSubscriptions)
            .build()
    }

    override suspend fun setTopicSubscriptions(userSubscriptions: List<SubscribedTopicProto>) {
        subscriptions = SubscriptionsProto.newBuilder()
            .clearSubscribedTopics()
            .addAllSubscribedTopics(userSubscriptions)
            .build()
    }

    override suspend fun clearProfile() {
        profile = ProfileProto.getDefaultInstance()
    }

    override suspend fun clearSubscriptions() {
        subscriptions = SubscriptionsProto.getDefaultInstance()
    }
}