package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toQueryScope
import com.kastik.apps.core.data.mappers.toSearchScope
import com.kastik.apps.core.data.mappers.toSort
import com.kastik.apps.core.data.mappers.toSortType
import com.kastik.apps.core.data.mappers.toTheme
import com.kastik.apps.core.data.mappers.toUserTheme
import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.SearchScope
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserPreferencesRepositoryImpl @Inject constructor(
    private val preferencesLocalDataSource: PreferencesLocalDataSource,
) : UserPreferencesRepository {
    override fun getHasSkippedSignIn(): Flow<Boolean> {
        return preferencesLocalDataSource.getHasSkippedSignIn()
    }

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) =
        withContext(Dispatchers.IO) {
        preferencesLocalDataSource.setHasSkippedSignIn(hasSkippedSignIn)
    }

    override fun getUserTheme(): Flow<UserTheme> {
        return preferencesLocalDataSource.getUserTheme().map { it.toUserTheme() }
    }

    override suspend fun setUserTheme(theme: UserTheme) = withContext(Dispatchers.IO) {
        preferencesLocalDataSource.setUserTheme(theme.toTheme())
    }

    override fun getDynamicColor(): Flow<Boolean> {
        return preferencesLocalDataSource.getDynamicColor()
    }

    override suspend fun setDynamicColor(enabled: Boolean) = withContext(Dispatchers.IO) {
        preferencesLocalDataSource.setDynamicColor(enabled)
    }

    override fun getSortType(): Flow<SortType> {
        return preferencesLocalDataSource.getSortType().map { it.toSortType() }
    }

    override suspend fun setSortType(sortType: SortType) = withContext(Dispatchers.IO) {
        preferencesLocalDataSource.setSortType(sortType.toSort())
    }

    override fun getSearchScope(): Flow<SearchScope> {
        return preferencesLocalDataSource.getSearchScope().map { it.toSearchScope() }
    }

    override suspend fun setSearchScope(searchScope: SearchScope) = withContext(Dispatchers.IO) {
        preferencesLocalDataSource.setSearchScope(searchScope.toQueryScope())
    }
}