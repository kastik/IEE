package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.Theme
import com.kastik.apps.core.model.user.UserPreferences
import kotlin.time.Instant
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>

    suspend fun setSkippedSignIn(isSkipped: Boolean)

    suspend fun setTheme(theme: Theme)

    suspend fun setDynamicColor(isEnabled: Boolean)

    suspend fun setSortType(sortType: SortType)

    suspend fun setSearchScope(searchScope: SearchScope)

    suspend fun setForYou(isEnabled: Boolean)

    suspend fun setFabFilters(areEnabled: Boolean)

    suspend fun setLastCheckTime(time: Instant?)

    suspend fun setCheckIntervalMinutes(minutes: Int)

    suspend fun increaseImportantEventCount()

    suspend fun resetImportantEventCount()
}
