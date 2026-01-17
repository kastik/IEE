package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.testing.api.FakeAboardApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

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
        val repository = tagsRemoteDataSource.fetchAnnouncementTags()
        assertEquals(remote, repository)
    }

    @Test
    fun fetchSubscribableTagsReturnsDataFromApi() = runTest {
        val remote = aboardClient.getUserSubscribableTags()
        val repository = tagsRemoteDataSource.fetchSubscribableTags()
        assertEquals(remote, repository)
    }
}