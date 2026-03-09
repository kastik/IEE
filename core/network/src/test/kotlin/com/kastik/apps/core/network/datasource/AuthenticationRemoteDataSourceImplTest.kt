package com.kastik.apps.core.network.datasource

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.network.api.FakeAboardApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFailsWith

class AuthenticationRemoteDataSourceImplTest {

    private val fakeAboardApiClient = FakeAboardApiClient()
    private val authenticationRemoteDataSourceImpl =
        AuthenticationRemoteDataSourceImpl(fakeAboardApiClient)

    @Test
    fun exchangeCodeForAboardTokenReturnDataFromApi() = runTest {
        val remote = fakeAboardApiClient.exchangeCodeForAboardToken("1234")
        val result = authenticationRemoteDataSourceImpl.exchangeCodeForAboardToken("1234")
        assertThat(result).isEqualTo(remote)
    }

    @Test
    fun exchangeCodeForAboardTokenDoesNotSwallowErrors() = runTest {
        fakeAboardApiClient.setThrowOnGetUserInfo(exception = IllegalStateException("Token expired"))
        assertFailsWith<IllegalStateException> {
            authenticationRemoteDataSourceImpl.exchangeCodeForAboardToken("12345")
        }
    }


    @Test
    fun checkIfTokenIsValidReturnsTrueWhenGetUserDoesNotThrowTest() = runTest {
        val result = authenticationRemoteDataSourceImpl.checkIfTokenIsValid()
        assertThat(result).isTrue()
    }

    @Test
    fun checkIfTokenIsValidReturnsFalseWhenGetUserThrowsTest() = runTest {
        fakeAboardApiClient.setThrowOnGetUserInfo(exception = IllegalStateException())

        assertFailsWith<IllegalStateException> {
            authenticationRemoteDataSourceImpl.checkIfTokenIsValid()
        }
    }
}

