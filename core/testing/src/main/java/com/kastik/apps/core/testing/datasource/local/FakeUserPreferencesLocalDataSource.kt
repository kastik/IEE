package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.datastore.proto.QueryScope
import com.kastik.apps.core.datastore.proto.Sort
import com.kastik.apps.core.datastore.proto.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesLocalDataSource : PreferencesLocalDataSource {
    var skipped: Boolean = false
    var theme: Theme = Theme.Light
    var dynamicColor: Boolean = true

    var sortType: Sort = Sort.Priority

    var enableForYou = false
    var queryScope: QueryScope = QueryScope.Title

    var notifiedAnnouncementIds: List<Int> = emptyList()


    override fun getHasSkippedSignIn(): Flow<Boolean> = flowOf(skipped)

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        skipped = hasSkippedSignIn
    }

    override fun getUserTheme(): Flow<Theme> {
        return flowOf(theme)
    }

    override suspend fun setUserTheme(theme: Theme) {
        this.theme = theme
    }

    override fun getDynamicColor(): Flow<Boolean> {
        return flowOf(dynamicColor)
    }

    override suspend fun setDynamicColor(value: Boolean) {
        dynamicColor = value
    }

    override fun getSortType(): Flow<Sort> {
        return flowOf(sortType)
    }

    override suspend fun setSortType(sortType: Sort) {
        this.sortType = sortType
    }

    override fun getSearchScope(): Flow<QueryScope> {
        return flowOf(queryScope)
    }

    override suspend fun setSearchScope(queryScope: QueryScope) {
        this.queryScope = queryScope
    }

    override fun getEnableForYou(): Flow<Boolean> {
        return flowOf(enableForYou)
    }

    override suspend fun setEnableForYou(value: Boolean) {
        this.enableForYou = value
    }

    override fun getNotifiedAnnouncementIds(): Flow<List<Int>> =
        flowOf(notifiedAnnouncementIds)


    override suspend fun setNotifiedAnnouncementIds(notificationIds: List<Int>) {
        notifiedAnnouncementIds = notificationIds
    }

}