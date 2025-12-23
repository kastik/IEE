package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toSort
import com.kastik.apps.core.data.mappers.toSortType
import com.kastik.apps.core.data.mappers.toTheme
import com.kastik.apps.core.data.mappers.toUserTheme
import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.di.UserPrefsDatastore
import com.kastik.apps.core.domain.repository.SortType
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserPreferencesRepoImpl @Inject constructor(
    @param:UserPrefsDatastore private val preferencesLocalDataSource: PreferencesLocalDataSource
) : UserPreferencesRepository {
    override fun getHasSkippedSignIn(): Flow<Boolean> {
        return preferencesLocalDataSource.getHasSkippedSignIn()
    }

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        preferencesLocalDataSource.setHasSkippedSignIn(hasSkippedSignIn)
    }

    override fun getUserTheme(): Flow<UserTheme> {
        return preferencesLocalDataSource.getUserTheme().map { it.toUserTheme() }
    }

    override suspend fun setUserTheme(theme: UserTheme) {
        preferencesLocalDataSource.setUserTheme(theme.toTheme())
    }

    override fun getDynamicColor(): Flow<Boolean> {
        return preferencesLocalDataSource.getDynamicColor()
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        preferencesLocalDataSource.setDynamicColor(enabled)
    }

    override fun getSortType(): Flow<SortType> {
        return preferencesLocalDataSource.getSortType().map { it.toSortType() }
    }

    override suspend fun setSortType(sortType: SortType) {
        preferencesLocalDataSource.setSortType(sortType.toSort())
    }
}