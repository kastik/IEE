package com.kastik.apps.core.network.datasource

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.network.api.FakeAboardApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Test


class AuthorRemoteDataSourceImplTest {
    private val fakeAboardApiClient = FakeAboardApiClient()
    private val authorRemoteDataSourceImpl = AuthorRemoteDataSourceImpl(fakeAboardApiClient)

    @Test
    fun fetchAuthorsReturnsDataFromApi() = runTest {
        val remote = fakeAboardApiClient.getAuthors()
        val result = authorRemoteDataSourceImpl.fetchAuthors()
        assertThat(result).isEqualTo(remote)

    }
}