package com.kastik.apps.core.data.repo

import com.kastik.apps.core.data.repository.AuthenticationRepositoryImpl
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.testing.datasource.local.FakeAuthenticationLocalDataSource
import com.kastik.apps.core.testing.datasource.remote.FakeAuthenticationRemoteDatasource
import com.kastik.apps.core.testing.testdata.userProfilesTestData
import com.kastik.apps.core.testing.testdata.userSubscribedTagDtoListTestData
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AuthenticationRepositoryImplTest {

    private lateinit var fakeLocalAuthDataSource: FakeAuthenticationLocalDataSource
    private lateinit var fakeRemoteAuthDataSource: FakeAuthenticationRemoteDatasource
    private lateinit var authenticationRepositoryImpl: AuthenticationRepository

    @Before
    fun setup() {
        fakeLocalAuthDataSource = FakeAuthenticationLocalDataSource()
        fakeRemoteAuthDataSource = FakeAuthenticationRemoteDatasource()
        authenticationRepositoryImpl =
            AuthenticationRepositoryImpl(fakeLocalAuthDataSource, fakeRemoteAuthDataSource)
    }

    @Test
    fun exchangeCodeForAppsTokenSavesResponseLocallyTest() = runTest {
        authenticationRepositoryImpl.exchangeCodeForAppsToken("code")
        assertNotNull(fakeLocalAuthDataSource.savedAppsAccessToken)
        assertNotNull(fakeLocalAuthDataSource.savedAppsRefreshToken)
    }

    @Test
    fun exchangeCodeForAboardTokenSavesResponseLocallyTest() = runTest {
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        assertNotNull(fakeLocalAuthDataSource.savedAboardAccessToken)
        assertNotNull(fakeLocalAuthDataSource.savedAboardExpiration)
    }

    @Test
    fun checkIfUserIsAuthenticatedReturnsFalseWhenNoTokenIssSavedTest() = runTest {
        val result = authenticationRepositoryImpl.checkIfUserIsAuthenticated()
        assertFalse(result)
    }

    @Test
    fun checkIfUserIsAuthenticatedReturnsTrueWhenLocalTokenExistsTest() = runTest {
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        assertTrue(authenticationRepositoryImpl.checkIfUserIsAuthenticated())
    }

    @Test
    fun getSavedTokenReturnsSavedAppsTokenTest() = runTest {
        authenticationRepositoryImpl.exchangeCodeForAppsToken("code")
        val result = authenticationRepositoryImpl.getSavedToken()
        assertNotNull(result)
    }

    @Test
    fun getUserProfileThrowsWhenNotLoggedIn() = runTest {
        assertFailsWith(
            exceptionClass = Exception::class,
            block = { authenticationRepositoryImpl.getUserProfile() }
        )
    }

    @Test
    fun getUserProfileReturnsProfileWhenLoggedIn() = runTest {
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        val result = authenticationRepositoryImpl.getUserProfile()
        assertEquals(userProfilesTestData.first().name, result.name)
        assertEquals(userProfilesTestData.first().email, result.email)
        assertEquals(userProfilesTestData.first().id, result.id)
    }

    @Test
    fun getUserSubscriptionsThrowsWhenNotLoggedIn() = runTest {
        assertFailsWith(
            exceptionClass = Exception::class,
            block = { authenticationRepositoryImpl.getUserSubscriptions() }
        )
    }

    @Test
    fun getUserSubscriptionsReturnsSubscribedTagsWhenLoggedIn() = runTest {
        authenticationRepositoryImpl.exchangeCodeForAbroadToken("code")
        val result = authenticationRepositoryImpl.getUserSubscriptions()
        assertEquals(userSubscribedTagDtoListTestData.size, result.size)
        assertEquals(userSubscribedTagDtoListTestData.first().id, result.first().id)
        assertEquals(userSubscribedTagDtoListTestData[1].title, result[1].title)
    }
}
