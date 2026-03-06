package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.network.api.FakeAboardApiClient
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

}