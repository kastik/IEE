package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.testing.api.FakeAboardApiClient
import com.kastik.apps.core.testing.api.FakeAppsApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthenticationRemoteDataSourceImplTest {

    private lateinit var appsClient: FakeAppsApiClient
    lateinit var aboardClient: FakeAboardApiClient
    private lateinit var authDatasource: AuthenticationRemoteDataSourceImpl

    @Before
    fun setUp() {
        appsClient = FakeAppsApiClient()
        aboardClient = FakeAboardApiClient()
        authDatasource = AuthenticationRemoteDataSourceImpl(
            appsApiClient = appsClient,
            aboardApiClient = aboardClient,
            clientId = "client",
            clientSecret = "secret"
        )
    }

    @Test
    fun checkIfTokenIsValidReturnsTrueWhenSuccessTest() = runTest {
        assertTrue(authDatasource.checkIfTokenIsValid())
    }

    @Test
    fun checkIfTokenIsValidReturnsFalseWhenExceptionThrownTest() = runTest {
        aboardClient.setThrowOnGetUserInfo(true)
        assertFalse(authDatasource.checkIfTokenIsValid())
    }

    @Test
    fun getUserProfileReturnsResponseTest() = runTest {
        //assertEquals(aboardClient.getUserInfo().id, authDatasource.getUserProfile().id)
    }

    @Test
    fun getUserSubscriptionsReturnsTagListTest() = runTest {
        //assertEquals(aboardClient.getUserSubscriptions(), authDatasource.getUserSubscriptions())
    }
}
