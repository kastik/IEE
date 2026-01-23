package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.datasource.local.FakeAuthenticationLocalDataSource
import com.kastik.apps.core.testing.datasource.remote.FakeAuthenticationRemoteDatasource
import com.kastik.apps.core.testing.testdata.aboardAuthTokenDtoTestData
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
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
        val response = aboardAuthTokenDtoTestData
        fakeAuthenticationRemoteDataSource.setAboardAccessTokenResponse(response)
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        assertThat(fakeAuthenticationLocalDataSource.aboardToken.value).isEqualTo(response.accessToken)
        assertThat(fakeAuthenticationLocalDataSource.aboardTokenExpiration.value).isEqualTo(response.expiresIn)
    }

    @Test
    fun exchangeCodeForAboardTokenThrowsWithWrongCodeTest() = runTest {
        fakeAuthenticationRemoteDataSource.throwOnApiRequest = RuntimeException("Error")
        assertFailsWith<RuntimeException> {
            authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        }

        /*
        TODO What should happen to local data when this happens?
         We should define behaviour for the different exception eg invalid code or no network exception
        assertThat(fakeAuthenticationLocalDataSource.savedAboardAccessToken).isEqualTo(response.accessToken)
        assertThat(fakeAuthenticationLocalDataSource.savedAboardExpiration).isEqualTo(response.expiresIn)
        */
    }

    @Test
    fun checkTokenValidityReturnsFalseWhenNoTokenIssSavedTest() = runTest {
        val result = authenticationRepositoryImpl.checkAboardTokenValidity()
        assertThat(result).isFalse()
    }

    @Test
    fun checkTokenValidityCallsApiWhenLocalTokenExistsTest() = runTest {
        fakeAuthenticationRemoteDataSource.setTokenValidity(true)
        fakeAuthenticationLocalDataSource.saveAboardToken("Token")
        val result = authenticationRepositoryImpl.checkAboardTokenValidity()
        assertThat(result).isTrue()

        fakeAuthenticationRemoteDataSource.setTokenValidity(false)
        fakeAuthenticationLocalDataSource.saveAboardToken("Token")
        val newResult = authenticationRepositoryImpl.checkAboardTokenValidity()
        assertThat(newResult).isFalse()
    }

    @Test
    fun getAboardTokenReturnsTokenFromDataSourceTest() = runTest {
        fakeAuthenticationRemoteDataSource.setAboardAccessTokenResponse(aboardAuthTokenDtoTestData)
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
        fakeAuthenticationLocalDataSource.saveAboardToken(token)
        val result = authenticationRepositoryImpl.getAboardToken()
        assertThat(result).isEqualTo(token)
    }

    @Test
    fun clearTokensClearsTokensFromDataSourceTest() = runTest {
        fakeAuthenticationRemoteDataSource.setAboardAccessTokenResponse(aboardAuthTokenDtoTestData)
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        authenticationRepositoryImpl.clearTokens()
        assertFalse(authenticationRepositoryImpl.checkAboardTokenValidity())
    }

    @Test
    fun clearTokensWhenNotLoggedInTest() = runTest {
        authenticationRepositoryImpl.clearTokens()
        assertFalse(authenticationRepositoryImpl.checkAboardTokenValidity())
    }

}
