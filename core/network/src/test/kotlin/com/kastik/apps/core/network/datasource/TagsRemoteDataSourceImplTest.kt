package com.kastik.apps.core.network.datasource

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.network.api.FakeAboardApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TagsRemoteDataSourceImplTest {
    lateinit var aboardClient: FakeAboardApiClient
    private lateinit var tagsRemoteDataSource: TagsRemoteDataSourceImpl

    @Before
    fun setUp() {
        aboardClient = FakeAboardApiClient()
        tagsRemoteDataSource = TagsRemoteDataSourceImpl(aboardClient)
    }

    @Test
    fun fetchTagsReturnsDataFromApi() = runTest {
        val remote = aboardClient.getTags()
        val result = tagsRemoteDataSource.fetchAnnouncementTags()
        assertThat(result).isEqualTo(remote)
    }

    @Test
    fun fetchSubscribableTagsReturnsDataFromApi() = runTest {
        val remote = aboardClient.getUserSubscribableTags()
        val result = tagsRemoteDataSource.fetchSubscribableTags()
        assertThat(result).containsExactlyElementsIn(remote)
    }

    @Test
    fun getSubscribedTagsReturnsDataFromApi() = runTest {
        val remote = aboardClient.getUserSubscriptions()
        val result = tagsRemoteDataSource.fetchSubscriptions()
        assertThat(result).containsExactlyElementsIn(remote)
    }

    @Test
    fun subscribeToEmailTagsForwardsCorrectIdsToApi() = runTest {
        val remote = listOf(1, 2, 3)
        tagsRemoteDataSource.subscribeToTags(remote)
        val result = aboardClient.getSubscribedIds()
        assertThat(result).containsExactlyElementsIn(remote)
    }

}