package com.kastik.apps.core.data.repository

import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.data.mappers.toSearchScopeProto
import com.kastik.apps.core.data.mappers.toSortTypeProto
import com.kastik.apps.core.data.mappers.toTheme
import com.kastik.apps.core.data.mappers.toTimestamp
import com.kastik.apps.core.data.mappers.toUserPreferences
import com.kastik.apps.core.datastore.datasource.PreferencesLocalDataSource
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Instant

@Singleton
internal class UserPreferencesRepositoryImpl @Inject constructor(
    private val preferencesLocalDataSource: PreferencesLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserPreferencesRepository {

    override val userPreferences =
        preferencesLocalDataSource.userPreferences.map { it.toUserPreferences() }

    override suspend fun setSkippedSignIn(isSkipped: Boolean) =
        withContext(ioDispatcher) {
            preferencesLocalDataSource.setSkippedSignIn(isSkipped)
        }

    override suspend fun setTheme(theme: Theme) = withContext(ioDispatcher) {
        preferencesLocalDataSource.setTheme(theme.toTheme())
    }

    override suspend fun setDynamicColor(isEnabled: Boolean) = withContext(ioDispatcher) {
        preferencesLocalDataSource.setDynamicColor(isEnabled)
    }

    override suspend fun setSortType(sortType: SortType) = withContext(ioDispatcher) {
        preferencesLocalDataSource.setSortType(sortType.toSortTypeProto())
    }

    override suspend fun setSearchScope(searchScope: SearchScope) = withContext(ioDispatcher) {
        preferencesLocalDataSource.setSearchScope(searchScope.toSearchScopeProto())
    }

    override suspend fun setForYou(isEnabled: Boolean) = withContext(ioDispatcher) {
        preferencesLocalDataSource.setForYou(isEnabled)
    }

    override suspend fun setFabFilters(areEnabled: Boolean) = withContext(ioDispatcher) {
        preferencesLocalDataSource.setFabFilters(areEnabled)
    }

    override suspend fun setLastCheckTime(time: Instant?) {
        preferencesLocalDataSource.setLastCheckTime(time?.toTimestamp())
    }

    override suspend fun setCheckIntervalMinutes(minutes: Int) {
        preferencesLocalDataSource.setCheckIntervalMinutes(minutes)
    }

    override suspend fun increaseImportantEventCount() {
        preferencesLocalDataSource.increaseImportantEventCount()
    }

    override suspend fun resetImportantEventCount() {
        preferencesLocalDataSource.resetImportantEventCount()
    }

}