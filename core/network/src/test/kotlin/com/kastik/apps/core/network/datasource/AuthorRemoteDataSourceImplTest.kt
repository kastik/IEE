package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.testing.api.FakeAboardApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class AuthorRemoteDataSourceImplTest {
    lateinit var aboardClient: FakeAboardApiClient
    private lateinit var authorRemoteDataSource: AuthorRemoteDataSourceImpl

    @Before
    fun setUp() {
        aboardClient = FakeAboardApiClient()
        authorRemoteDataSource = AuthorRemoteDataSourceImpl(aboardClient)
    }

    @Test
    fun fetchAuthorsReturnsDataFromApi() = runTest {
        val remote = aboardClient.getAuthors()
        val repository = authorRemoteDataSource.fetchAuthors()
        assertEquals(remote, repository)
    }
}