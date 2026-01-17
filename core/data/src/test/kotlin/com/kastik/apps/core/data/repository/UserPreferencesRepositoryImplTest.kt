package com.kastik.apps.core.data.repository


import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.UserTheme
import com.kastik.apps.core.testing.datasource.local.FakeUserPreferencesLocalDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserPreferencesRepositoryImplTest {

    private lateinit var preferencesRepositoryImpl: UserPreferencesRepositoryImpl
    private lateinit var fakeUserPreferencesLocalDataSource: FakeUserPreferencesLocalDataSource

    @Before
    fun setup() {
        fakeUserPreferencesLocalDataSource = FakeUserPreferencesLocalDataSource()
        preferencesRepositoryImpl = UserPreferencesRepositoryImpl(
            preferencesLocalDataSource = fakeUserPreferencesLocalDataSource,
        )
    }

    @Test
    fun getHasSkippedSignInSetsAndReturnsTheProperValueTest() = runTest {
        preferencesRepositoryImpl.setHasSkippedSignIn(true)
        assertThat(preferencesRepositoryImpl.getHasSkippedSignIn().first()).isTrue()
        preferencesRepositoryImpl.setHasSkippedSignIn(false)
        assertThat(preferencesRepositoryImpl.getHasSkippedSignIn().first()).isFalse()
    }

    @Test
    fun getHasSkippedSignInReturnsFalseWhenNoValueIsSet() = runTest {
        assertThat(preferencesRepositoryImpl.getHasSkippedSignIn().first()).isFalse()
    }

    @Test
    fun getUserThemeSetsAndReturnsTheProperValueTest() = runTest {
        preferencesRepositoryImpl.setUserTheme(UserTheme.DARK)
        assertThat(preferencesRepositoryImpl.getUserTheme().first()).isEqualTo(UserTheme.DARK)
        preferencesRepositoryImpl.setUserTheme(UserTheme.LIGHT)
        assertThat(preferencesRepositoryImpl.getUserTheme().first()).isEqualTo(UserTheme.LIGHT)
        preferencesRepositoryImpl.setUserTheme(UserTheme.FOLLOW_SYSTEM)
        assertThat(
            preferencesRepositoryImpl.getUserTheme().first()
        ).isEqualTo(UserTheme.FOLLOW_SYSTEM)
    }

    @Test
    fun getDynamicColorSetsAndReturnsTheProperValueTest() = runTest {
        preferencesRepositoryImpl.setDynamicColor(true)
        assertThat(preferencesRepositoryImpl.getDynamicColor().first()).isTrue()

        preferencesRepositoryImpl.setDynamicColor(false)
        assertThat(preferencesRepositoryImpl.getDynamicColor().first()).isFalse()
    }

    @Test
    fun getSortTypeSetsAndReturnsTheProperValueTest() = runTest {
        // Replace 'SortType.NAME' with a valid value from your SortType enum/sealed class
        val sortType = SortType.Priority
        preferencesRepositoryImpl.setSortType(sortType)
        assertThat(preferencesRepositoryImpl.getSortType().first()).isEqualTo(sortType)
    }
}
