package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.FakeAboardApiClient
import com.kastik.apps.core.network.model.request.SubscribeDto

class FakeTagsRemoteDataSource : TagsRemoteDataSource {

    val fakeAboardApiClient = FakeAboardApiClient()

    override suspend fun fetchAnnouncementTags() = fakeAboardApiClient.getTags()

    override suspend fun fetchSubscribableTags() = fakeAboardApiClient.getAvailableTags()

    override suspend fun fetchSubscriptions() = fakeAboardApiClient.getSubscribedTags()

    override suspend fun subscribeToTags(tagIds: List<Int>) =
        fakeAboardApiClient.subscribeToTags(SubscribeDto(tagIds))
}
