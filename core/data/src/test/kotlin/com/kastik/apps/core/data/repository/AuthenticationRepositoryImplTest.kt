package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.datasource.local.FakeAuthenticationLocalDataSource
import com.kastik.apps.core.testing.datasource.remote.FakeAuthenticationRemoteDatasource
import com.kastik.apps.core.testing.testdata.aboardTokenResponseDtoTestData
import com.kastik.apps.core.testing.utils.FakeCrashlytics
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private val fakeAuthenticationLocalDataSource = FakeAuthenticationLocalDataSource()
    private val fakeAuthenticationRemoteDataSource = FakeAuthenticationRemoteDatasource()
    private lateinit var authenticationRepositoryImpl: AuthenticationRepositoryImpl

    @Before
    fun setup() {
        authenticationRepositoryImpl = AuthenticationRepositoryImpl(
            crashlytics = FakeCrashlytics(),
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
            authenticationRemoteDataSource = fakeAuthenticationRemoteDataSource,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun exchangeCodeForAboardTokenSavesResponseLocallyTest() = runTest(testDispatcher) {
        val response = aboardTokenResponseDtoTestData
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        assertThat(fakeAuthenticationLocalDataSource.aboardToken.value).isEqualTo(response.accessToken)
        assertThat(fakeAuthenticationLocalDataSource.aboardTokenExpiration.value).isEqualTo(response.expiresIn)
    }

    @Test
    fun exchangeCodeForAboardUpdatesIsSignInTest() = runTest(testDispatcher) {
        val response = aboardTokenResponseDtoTestData
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        assertThat(fakeAuthenticationLocalDataSource.isSignedIn.value).isTrue()
    }


    @Test
    fun clearTokensClearsLocalDataFromDataSourceTest() = runTest(testDispatcher) {
        val response = aboardTokenResponseDtoTestData
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        authenticationRepositoryImpl.clearAuthenticationData()
        assertThat(fakeAuthenticationLocalDataSource.aboardToken.value).isNull()
        assertThat(fakeAuthenticationLocalDataSource.aboardTokenExpiration.value).isNull()
        assertThat(fakeAuthenticationLocalDataSource.aboardTokenLastRefreshTime.value).isNull()
        assertThat(fakeAuthenticationLocalDataSource.isSignedIn.value).isFalse()
    }
}
