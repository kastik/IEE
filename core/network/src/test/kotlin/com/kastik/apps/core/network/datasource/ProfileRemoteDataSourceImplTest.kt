package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.FakeAboardApiClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ProfileRemoteDataSourceImplTest {
    lateinit var aboardClient: FakeAboardApiClient
    private lateinit var profileRemoteDataSource: ProfileRemoteDataSourceImpl

    @Before
    fun setUp() {
        aboardClient = FakeAboardApiClient()
        profileRemoteDataSource = ProfileRemoteDataSourceImpl(aboardClient)
    }

    @Test
    fun getProfileReturnsDataFromApi() = runTest {
        val remote = aboardClient.getUserInfo()
        val repository = profileRemoteDataSource.getProfile()
        assertEquals(remote, repository)
    }

    @Test
    fun getSubscribedTagsReturnsDataFromApi() = runTest {
        val remote = aboardClient.getUserSubscriptions()
        val repository = profileRemoteDataSource.getEmailSubscriptions()
        assertEquals(remote, repository)
    }

    @Test
    fun subscribeToEmailTagsForwardsCorrectIdsToApi() = runTest {
        val subscribeToIds = listOf(1, 2, 3)
        profileRemoteDataSource.subscribeToEmailTags(subscribeToIds)
        val result = aboardClient.getSubscribedIds().first()
        assertEquals(subscribeToIds, result)
    }
}