package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

interface UserPreferencesRepository {
    fun hasSkippedSignIn(): Flow<Boolean>
    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean)
    fun getTheme(): Flow<UserTheme>
    suspend fun setTheme(theme: UserTheme)
    fun isDynamicColorEnabled(): Flow<Boolean>
    suspend fun setDynamicColorEnabled(enabled: Boolean)
    fun getSortType(): Flow<SortType>
    suspend fun setSortType(sortType: SortType)
    fun getSearchScope(): Flow<SearchScope>
    suspend fun setSearchScope(searchScope: SearchScope)
    fun isForYouEnabled(): Flow<Boolean>
    suspend fun setForYouEnabled(value: Boolean)
    fun areFabFiltersEnabled(): Flow<Boolean>
    suspend fun setFabFiltersEnabled(value: Boolean)
    fun getLastNotificationCheckTime(): Flow<Instant?>
    suspend fun setLastNotificationCheckTime(time: Instant?)
}