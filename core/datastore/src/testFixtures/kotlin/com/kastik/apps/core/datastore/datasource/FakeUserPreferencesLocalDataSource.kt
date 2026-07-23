package com.kastik.apps.core.datastore.datasource

import com.google.protobuf.Timestamp
import com.kastik.apps.core.datastore.proto.SearchScopeProto
import com.kastik.apps.core.datastore.proto.SortTypeProto
import com.kastik.apps.core.datastore.proto.ThemeProto
import com.kastik.apps.core.datastore.proto.UserPreferencesProto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesLocalDataSource() : PreferencesLocalDataSource {

    private val _userPreferences =
        MutableStateFlow<UserPreferencesProto>(UserPreferencesProto.getDefaultInstance())

    override val userPreferences = _userPreferences.asStateFlow()

    override suspend fun setSkippedSignIn(isSkipped: Boolean) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setHasSkippedSignIn(isSkipped).build()
        }
    }

    override suspend fun setTheme(theme: ThemeProto) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setTheme(theme).build()
        }
    }

    override suspend fun setDynamicColor(isEnabled: Boolean) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setIsDynamicColorEnabled(isEnabled).build()
        }
    }

    override suspend fun setSortType(sortType: SortTypeProto) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setSortType(sortType).build()
        }
    }

    override suspend fun setSearchScope(searchScope: SearchScopeProto) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setSearchScope(searchScope).build()
        }
    }

    override suspend fun setForYou(isEnabled: Boolean) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setIsForYouEnabled(isEnabled).build()
        }
    }

    override suspend fun setFabFilters(areEnabled: Boolean) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setAreFabFiltersEnabled(areEnabled).build()
        }
    }

    override suspend fun setLastCheckTime(time: Timestamp?) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setLastCheckTime(time).build()
        }
    }

    override suspend fun setCheckIntervalMinutes(minutes: Int) {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setCheckIntervalMinutes(minutes).build()
        }
    }

    override suspend fun increaseImportantEventCount() {
        _userPreferences.update { userPreferences ->
            userPreferences
                .toBuilder()
                .setImportantEventCount(userPreferences.importantEventCount + 1)
                .build()
        }
    }

    override suspend fun resetImportantEventCount() {
        _userPreferences.update { userPreferences ->
            userPreferences.toBuilder().setImportantEventCount(0).build()
        }
    }
}
