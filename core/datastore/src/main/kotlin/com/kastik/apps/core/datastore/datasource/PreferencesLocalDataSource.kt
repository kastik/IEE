package com.kastik.apps.core.datastore.datasource

import androidx.datastore.core.DataStore
import com.google.protobuf.Timestamp
import com.kastik.apps.core.datastore.di.UserPrefsDatastore
import com.kastik.apps.core.datastore.proto.SearchScopeProto
import com.kastik.apps.core.datastore.proto.SortTypeProto
import com.kastik.apps.core.datastore.proto.ThemeProto
import com.kastik.apps.core.datastore.proto.UserPreferencesProto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface PreferencesLocalDataSource {
    val userPreferences: Flow<UserPreferencesProto>
    suspend fun setSkippedSignIn(isSkipped: Boolean)
    suspend fun setTheme(theme: ThemeProto)
    suspend fun setDynamicColor(isEnabled: Boolean)
    suspend fun setSortType(sortType: SortTypeProto)
    suspend fun setSearchScope(searchScope: SearchScopeProto)
    suspend fun setForYou(isEnabled: Boolean)
    suspend fun setFabFilters(areEnabled: Boolean)
    suspend fun setLastCheckTime(time: Timestamp?)
    suspend fun setCheckIntervalMinutes(minutes: Int)
    suspend fun increaseImportantEventCount()
    suspend fun resetImportantEventCount()
}

@Singleton
internal class PreferencesLocalDataSourceImpl @Inject constructor(
    @UserPrefsDatastore private val dataStore: DataStore<UserPreferencesProto>
) : PreferencesLocalDataSource {

    override val userPreferences: Flow<UserPreferencesProto> = dataStore.data

    override suspend fun setSkippedSignIn(isSkipped: Boolean) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setHasSkippedSignIn(isSkipped)
                .build()
        }
    }


    override suspend fun setTheme(theme: ThemeProto) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setTheme(theme)
                .build()
        }
    }


    override suspend fun setDynamicColor(isEnabled: Boolean) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setIsDynamicColorEnabled(isEnabled)
                .build()
        }
    }

    override suspend fun setSortType(sortType: SortTypeProto) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setSortType(sortType)
                .build()
        }
    }


    override suspend fun setSearchScope(searchScope: SearchScopeProto) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setSearchScope(searchScope)
                .build()
        }
    }

    override suspend fun setForYou(isEnabled: Boolean) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setIsForYouEnabled(isEnabled)
                .build()
        }
    }

    override suspend fun setFabFilters(areEnabled: Boolean) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setAreFabFiltersEnabled(areEnabled)
                .build()
        }
    }

    override suspend fun setLastCheckTime(time: Timestamp?) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setLastCheckTime(time)
                .build()
        }
    }

    override suspend fun setCheckIntervalMinutes(minutes: Int) {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setCheckIntervalMinutes(minutes)
                .build()
        }
    }

    override suspend fun increaseImportantEventCount() {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setImportantEventCount(userPreferences.importantEventCount + 1)
                .build()
        }
    }

    override suspend fun resetImportantEventCount() {
        dataStore.updateData { userPreferences ->
            userPreferences.toBuilder()
                .setImportantEventCount(0)
                .build()
        }
    }

}