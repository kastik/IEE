package com.kastik.apps.core.data.repository


import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.UserTheme
import com.kastik.apps.core.testing.datasource.local.FakeUserPreferencesLocalDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserPreferencesRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var preferencesRepositoryImpl: UserPreferencesRepositoryImpl
    private lateinit var fakeUserPreferencesLocalDataSource: FakeUserPreferencesLocalDataSource

    @Before
    fun setup() {
        fakeUserPreferencesLocalDataSource = FakeUserPreferencesLocalDataSource()
        preferencesRepositoryImpl = UserPreferencesRepositoryImpl(
            preferencesLocalDataSource = fakeUserPreferencesLocalDataSource,
            ioDispatcher = testDispatcher
        )
    }

    @Test
    fun getHasSkippedSignInSetsAndReturnsTheProperValueTest() = runTest(testDispatcher) {
        preferencesRepositoryImpl.setHasSkippedSignIn(true)
        assertThat(preferencesRepositoryImpl.hasSkippedSignIn().first()).isTrue()
        preferencesRepositoryImpl.setHasSkippedSignIn(false)
        assertThat(preferencesRepositoryImpl.hasSkippedSignIn().first()).isFalse()
    }

    @Test
    fun getHasSkippedSignInReturnsFalseWhenNoValueIsSet() = runTest(testDispatcher) {
        assertThat(preferencesRepositoryImpl.hasSkippedSignIn().first()).isFalse()
    }

    @Test
    fun getUserThemeSetsAndReturnsTheProperValueTest() = runTest(testDispatcher) {
        preferencesRepositoryImpl.setTheme(UserTheme.DARK)
        assertThat(preferencesRepositoryImpl.getTheme().first()).isEqualTo(UserTheme.DARK)
        preferencesRepositoryImpl.setTheme(UserTheme.LIGHT)
        assertThat(preferencesRepositoryImpl.getTheme().first()).isEqualTo(UserTheme.LIGHT)
        preferencesRepositoryImpl.setTheme(UserTheme.FOLLOW_SYSTEM)
        assertThat(
            preferencesRepositoryImpl.getTheme().first()
        ).isEqualTo(UserTheme.FOLLOW_SYSTEM)
    }

    @Test
    fun getDynamicColorSetsAndReturnsTheProperValueTest() = runTest(testDispatcher) {
        preferencesRepositoryImpl.setDynamicColorEnabled(true)
        assertThat(preferencesRepositoryImpl.isDynamicColorEnabled().first()).isTrue()

        preferencesRepositoryImpl.setDynamicColorEnabled(false)
        assertThat(preferencesRepositoryImpl.isDynamicColorEnabled().first()).isFalse()
    }

    @Test
    fun getSortTypeSetsAndReturnsTheProperValueTest() = runTest(testDispatcher) {
        val sortType = SortType.Priority
        preferencesRepositoryImpl.setSortType(sortType)
        assertThat(preferencesRepositoryImpl.getSortType().first()).isEqualTo(sortType)
    }
}
