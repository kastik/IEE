package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.crashlytics.FakeCrashlytics
import com.kastik.apps.core.datastore.datasource.FakeAuthenticationLocalDataSource
import com.kastik.apps.core.network.datasource.FakeAuthenticationRemoteDatasource
import com.kastik.apps.core.network.testdata.baseTokenDto
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeCrashlytics: FakeCrashlytics = FakeCrashlytics()
    private val fakeAuthenticationLocalDataSource = FakeAuthenticationLocalDataSource()
    private val fakeAuthenticationRemoteDataSource = FakeAuthenticationRemoteDatasource()
    private lateinit var authenticationRepositoryImpl: AuthenticationRepositoryImpl

    @Before
    fun setup() {
        authenticationRepositoryImpl = AuthenticationRepositoryImpl(
            crashlytics = fakeCrashlytics,
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
            authenticationRemoteDataSource = fakeAuthenticationRemoteDataSource,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun exchangeCodeForAboardTokenSavesResponseLocallyTest() = runTest(testDispatcher) {
        val response = baseTokenDto
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response

        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")

        val result = fakeAuthenticationLocalDataSource.aboardAccessToken.value
        assertThat(result).isEqualTo(response.accessToken)
    }

    @Test
    fun exchangeCodeForAboardUpdatesIsSignInTest() = runTest(testDispatcher) {
        val response = baseTokenDto
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response

        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")

        val result = fakeAuthenticationLocalDataSource.isSignedIn.value
        assertThat(result).isTrue()
    }


    @Test
    fun clearTokensClearsLocalDataFromDataSourceTest() = runTest(testDispatcher) {
        val response = baseTokenDto
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response

        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        authenticationRepositoryImpl.clearAuthenticationData()

        val tokenResult = fakeAuthenticationLocalDataSource.aboardAccessToken.value
        val isSignedInResult = fakeAuthenticationLocalDataSource.isSignedIn.value

        assertThat(tokenResult).isNull()
        assertThat(isSignedInResult).isFalse()
    }
}
