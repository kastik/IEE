package com.kastik.apps.core.network.datasource

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.network.api.FakeAboardApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationRemoteDataSourceImplTest {

    lateinit var aboardClient: FakeAboardApiClient
    private lateinit var authDatasource: AuthenticationRemoteDataSourceImpl

    @Before
    fun setUp() {
        aboardClient = FakeAboardApiClient()
        authDatasource = AuthenticationRemoteDataSourceImpl(
            aboardApiClient = aboardClient,
        )
    }

    @Test
    fun checkIfTokenIsValidReturnsTrueWhenGetUserDoesNotThrowTest() = runTest {
        val result = authDatasource.checkIfTokenIsValid()
        assertThat(result).isTrue()
    }

    @Test
    fun checkIfTokenIsValidReturnsFalseWhenGetUserThrowsTest() = runTest {
        aboardClient.setThrowOnGetUserInfo(exception = IllegalStateException())
        val result = authDatasource.checkIfTokenIsValid()
        assertThat(result).isFalse()
    }

}
