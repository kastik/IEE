package com.kastik.apps.core.data.repository

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.data.mappers.toSearchScopeProto
import com.kastik.apps.core.data.mappers.toSortTypeProto
import com.kastik.apps.core.data.mappers.toThemeProto
import com.kastik.apps.core.data.mappers.toTimestamp
import com.kastik.apps.core.data.mappers.toUserPreferences
import com.kastik.apps.core.datastore.datasource.FakeUserPreferencesLocalDataSource
import com.kastik.apps.core.datastore.testdata.baseUserPreferencesProto
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock

class UserPreferencesRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var preferencesRepositoryImpl: UserPreferencesRepositoryImpl
    private lateinit var fakeUserPreferencesLocalDataSource: FakeUserPreferencesLocalDataSource

    @Before
    fun setup() {
        fakeUserPreferencesLocalDataSource = FakeUserPreferencesLocalDataSource()
        preferencesRepositoryImpl =
            UserPreferencesRepositoryImpl(
                preferencesLocalDataSource = fakeUserPreferencesLocalDataSource,
                ioDispatcher = testDispatcher,
            )
    }

    @Test
    fun userPreferencesFlowEmitsMappedDomainPreferences() =
        runTest(testDispatcher) {
            val result = preferencesRepositoryImpl.userPreferences.first()

            assertThat(result).isEqualTo(baseUserPreferencesProto.toUserPreferences())
        }

    @Test
    fun setSkippedSignInUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            preferencesRepositoryImpl.setSkippedSignIn(true)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            val result = userPreferences.hasSkippedSignIn
            assertThat(result).isTrue()
        }

    @Test
    fun setThemeUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            val theme = Theme.DARK

            preferencesRepositoryImpl.setTheme(theme)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.theme).isEqualTo(theme.toThemeProto())
        }

    @Test
    fun setDynamicColorUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            preferencesRepositoryImpl.setDynamicColor(true)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.isDynamicColorEnabled).isTrue()
        }

    @Test
    fun setSortTypeUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            val sortType = SortType.Priority

            preferencesRepositoryImpl.setSortType(sortType)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.sortType).isEqualTo(sortType.toSortTypeProto())
        }

    @Test
    fun setSearchScopeUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            val searchScope = SearchScope.TitleAndBody

            preferencesRepositoryImpl.setSearchScope(searchScope)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.searchScope).isEqualTo(searchScope.toSearchScopeProto())
        }

    @Test
    fun setForYouUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            preferencesRepositoryImpl.setForYou(true)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.isForYouEnabled).isTrue()
        }

    @Test
    fun setFabFiltersUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            preferencesRepositoryImpl.setFabFilters(true)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.areFabFiltersEnabled).isTrue()
        }

    @Test
    fun setLastCheckTimeWithValidInstantUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            val now = Clock.System.now()

            preferencesRepositoryImpl.setLastCheckTime(now)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.lastCheckTime).isEqualTo(now.toTimestamp())
        }

    @Test
    fun setCheckIntervalMinutesUpdatesLocalDataSource() =
        runTest(testDispatcher) {
            val minutes = 15

            preferencesRepositoryImpl.setCheckIntervalMinutes(minutes)

            val userPreferences = fakeUserPreferencesLocalDataSource.userPreferences.value
            assertThat(userPreferences.checkIntervalMinutes).isEqualTo(minutes)
        }

    @Test
    fun increaseImportantEventCountIncrementsLocalDataSourceCounter() =
        runTest(testDispatcher) {
            val initialCount =
                fakeUserPreferencesLocalDataSource.userPreferences.value.importantEventCount
            assertThat(initialCount).isEqualTo(0)

            preferencesRepositoryImpl.increaseImportantEventCount()
            preferencesRepositoryImpl.increaseImportantEventCount()

            val updatedCount =
                fakeUserPreferencesLocalDataSource.userPreferences.value.importantEventCount
            assertThat(updatedCount).isEqualTo(2)
        }

    @Test
    fun resetImportantEventCountResetsLocalDataSourceCounter() =
        runTest(testDispatcher) {
            preferencesRepositoryImpl.increaseImportantEventCount()
            assertThat(fakeUserPreferencesLocalDataSource.userPreferences.value.importantEventCount)
                .isEqualTo(1)

            preferencesRepositoryImpl.resetImportantEventCount()

            val updatedCount =
                fakeUserPreferencesLocalDataSource.userPreferences.value.importantEventCount
            assertThat(updatedCount).isEqualTo(0)
        }
}
