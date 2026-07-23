package com.kastik.apps.core.network.datasource

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.network.api.FakeAboardApiClient
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthenticationRemoteDataSourceImplTest {

    private val fakeAboardApiClient = FakeAboardApiClient()
    private val authenticationRemoteDataSourceImpl =
        AuthenticationRemoteDataSourceImpl(fakeAboardApiClient)

    @Test
    fun exchangeCodeForAboardTokenReturnDataFromApi() = runTest {
        val remote = fakeAboardApiClient.exchangeAuthCode("1234")
        val result = authenticationRemoteDataSourceImpl.exchangeCodeForAboardToken("1234")
        assertThat(result).isEqualTo(remote)
    }

    @Test
    fun exchangeCodeForAboardTokenDoesNotSwallowErrors() = runTest {
        fakeAboardApiClient.setThrowOnGetUserInfo(
            exception = IllegalStateException("Token expired")
        )
        assertFailsWith<IllegalStateException> {
            authenticationRemoteDataSourceImpl.exchangeCodeForAboardToken("12345")
        }
    }
}
