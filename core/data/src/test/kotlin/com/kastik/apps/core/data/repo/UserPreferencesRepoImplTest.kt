package com.kastik.apps.core.data.repo


import com.kastik.apps.core.data.repository.UserPreferencesRepoImpl
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.testing.datasource.local.FakeUserPreferencesLocalDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserPreferencesRepoImplTest {

    private lateinit var fakeUserPreferencesLocalDataSource: FakeUserPreferencesLocalDataSource
    private lateinit var preferencesRepositoryImpl: UserPreferencesRepository

    @Before
    fun setup() {
        fakeUserPreferencesLocalDataSource = FakeUserPreferencesLocalDataSource()
        preferencesRepositoryImpl = UserPreferencesRepoImpl(fakeUserPreferencesLocalDataSource)
    }

    @Test
    fun getHasSkippedSignInSetsAndReturnsTheProperValueTest() = runTest {
        preferencesRepositoryImpl.setHasSkippedSignIn(true)
        assertTrue(preferencesRepositoryImpl.getHasSkippedSignIn())

        preferencesRepositoryImpl.setHasSkippedSignIn(false)
        assertFalse(preferencesRepositoryImpl.getHasSkippedSignIn())
    }

    @Test
    fun getHasSkippedSignInReturnsFalseWhenNoValueIsSet() = runTest {
        assertFalse(preferencesRepositoryImpl.getHasSkippedSignIn())
    }
}
