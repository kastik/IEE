package com.kastik.apps.core.domain.repository

import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun getHasSkippedSignIn(): Flow<Boolean>
    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean)
    fun getUserTheme(): Flow<UserTheme>
    suspend fun setUserTheme(theme: UserTheme)
    fun getDynamicColor(): Flow<Boolean>
    suspend fun setDynamicColor(enabled: Boolean)
    fun getSortType(): Flow<SortType>
    suspend fun setSortType(sortType: SortType)
}