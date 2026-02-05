package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.datasource.local.FakeAuthenticationLocalDataSource
import com.kastik.apps.core.testing.datasource.remote.FakeAuthenticationRemoteDatasource
import com.kastik.apps.core.testing.testdata.aboardTokenResponseDtoTestData
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class AuthenticationRepositoryImplTest {

    private val fakeAuthenticationLocalDataSource = FakeAuthenticationLocalDataSource()
    private val fakeAuthenticationRemoteDataSource = FakeAuthenticationRemoteDatasource()
    private lateinit var authenticationRepositoryImpl: AuthenticationRepositoryImpl

    @Before
    fun setup() {
        authenticationRepositoryImpl = AuthenticationRepositoryImpl(
            authenticationLocalDataSource = fakeAuthenticationLocalDataSource,
            authenticationRemoteDataSource = fakeAuthenticationRemoteDataSource,
        )
    }

    @Test
    fun exchangeCodeForAboardTokenSavesResponseLocallyTest() = runTest {
        val response = aboardTokenResponseDtoTestData
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        assertThat(fakeAuthenticationLocalDataSource.aboardToken.value).isEqualTo(response.accessToken)
        assertThat(fakeAuthenticationLocalDataSource.aboardTokenExpiration.value).isEqualTo(response.expiresIn)
    }

    @Test
    fun exchangeCodeForAboardUpdatesIsSignInTest() = runTest {
        val response = aboardTokenResponseDtoTestData
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        assertThat(fakeAuthenticationLocalDataSource.isSignedIn.value).isTrue()
    }

    @Test
    fun exchangeCodeForAboardTokenThrowsWithWrongCodeTest() = runTest {
        fakeAuthenticationRemoteDataSource.throwOnApiRequest = RuntimeException("Error")
        assertFailsWith<RuntimeException> {
            authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        }
    }

    @Test
    fun getAboardTokenReturnsTokenFromDataSourceTest() = runTest {
        val response = aboardTokenResponseDtoTestData
        fakeAuthenticationRemoteDataSource.aboardAccessTokenResponse = response
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")

        val result = authenticationRepositoryImpl.getAboardToken()
        assertNotNull(result)
    }

    @Test
    fun getAboardTokenReturnsNullWhenNotSet() = runTest {
        val result = authenticationRepositoryImpl.getAboardToken()
        assertThat(result).isNull()
    }

    @Test
    fun getAboardTokenReturnsTokenWhenSet() = runTest {
        val token = "Token"
        fakeAuthenticationLocalDataSource.setAboardAccessToken(token)
        val result = authenticationRepositoryImpl.getAboardToken()
        assertThat(result).isEqualTo(token)
    }

    @Test
    fun clearTokensClearsLocalDataFromDataSourceTest() = runTest {
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
