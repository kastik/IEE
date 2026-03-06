package com.kastik.apps.core.datastore.datasource

import com.google.protobuf.Timestamp
import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.datastore.proto.QueryScope
import com.kastik.apps.core.datastore.proto.Sort
import com.kastik.apps.core.datastore.proto.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesLocalDataSource : PreferencesLocalDataSource {

    private val _hasSkippedSignIn = MutableStateFlow(false)
    val hasSkippedSignIn = _hasSkippedSignIn.asStateFlow()
    private val _theme = MutableStateFlow(Theme.Light)
    val theme = _theme.asStateFlow()
    private val _dynamicColor = MutableStateFlow(true)
    val dynamicColor = _dynamicColor.asStateFlow()
    private val _sortType = MutableStateFlow(Sort.Priority)
    val sortType = _sortType.asStateFlow()
    private val _fabFiltersEnabled = MutableStateFlow(true)
    val fabFiltersEnabled = _fabFiltersEnabled.asStateFlow()
    private val _enableForYou = MutableStateFlow(false)
    val enableForYou = _enableForYou.asStateFlow()
    private val _queryScope = MutableStateFlow(QueryScope.Title)
    val queryScope = _queryScope.asStateFlow()
    private val _lastNotifiedTime = MutableStateFlow<Timestamp?>(null)
    val lastNotifiedTime = _lastNotifiedTime.asStateFlow()
    private val _announcementCheckIntervalMinutes = MutableStateFlow(15)
    val announcementCheckIntervalMinutes = _announcementCheckIntervalMinutes.asStateFlow()


    override fun getHasSkippedSignIn(): Flow<Boolean> = hasSkippedSignIn

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        _hasSkippedSignIn.update {
            hasSkippedSignIn
        }
    }

    override fun getUserTheme(): Flow<Theme> = theme


    override suspend fun setUserTheme(theme: Theme) {
        theme.let {
            _theme.update { theme }
        }
    }

    override fun getDynamicColor(): Flow<Boolean> = dynamicColor


    override suspend fun setDynamicColor(value: Boolean) {
        _dynamicColor.update {
            value
        }
    }

    override fun getSortType(): Flow<Sort> = sortType


    override suspend fun setSortType(sortType: Sort) {
        _sortType.update {
            sortType
        }
    }

    override fun getSearchScope(): Flow<QueryScope> = queryScope


    override suspend fun setSearchScope(queryScope: QueryScope) {
        _queryScope.update {
            queryScope
        }
    }

    override fun getEnableForYou(): Flow<Boolean> = enableForYou


    override suspend fun setEnableForYou(value: Boolean) {
        _enableForYou.update {
            value
        }
    }


    override fun areFabFiltersEnabled(): Flow<Boolean> = fabFiltersEnabled


    override suspend fun setAreFabFiltersEnabled(value: Boolean) {
        _fabFiltersEnabled.update {
            value
        }
    }

    override fun getLastNotificationCheckTime(): Flow<Timestamp?> = lastNotifiedTime


    override suspend fun setLastNotificationCheckTime(time: Timestamp?) {
        _lastNotifiedTime.update {
            time
        }
    }

    override fun getAnnouncementCheckIntervalMinutes(): Flow<Int> = announcementCheckIntervalMinutes

    override suspend fun setAnnouncementCheckIntervalMinutes(minutes: Int) {
        _announcementCheckIntervalMinutes.update {
            minutes
        }
    }

}