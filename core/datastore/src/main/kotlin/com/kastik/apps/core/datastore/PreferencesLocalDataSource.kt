package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.QueryScope
import com.kastik.apps.core.datastore.proto.Sort
import com.kastik.apps.core.datastore.proto.Theme
import com.kastik.apps.core.datastore.proto.UserPreferences
import com.kastik.apps.core.di.UserPrefsDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PreferencesLocalDataSource {
    fun getHasSkippedSignIn(): Flow<Boolean>
    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean)
    fun getUserTheme(): Flow<Theme>
    suspend fun setUserTheme(theme: Theme)
    fun getDynamicColor(): Flow<Boolean>
    suspend fun setDynamicColor(value: Boolean)
    fun getSortType(): Flow<Sort>
    suspend fun setSortType(sortType: Sort)
    fun getSearchScope(): Flow<QueryScope>
    suspend fun setSearchScope(queryScope: QueryScope)
    fun getEnableForYou(): Flow<Boolean>
    suspend fun setEnableForYou(value: Boolean)
    fun getNotifiedAnnouncementIds(): Flow<List<Int>>
    suspend fun setNotifiedAnnouncementId(notificationId: Int)
    suspend fun setNotifiedAnnouncementId(notificationIds: List<Int>)
    suspend fun clearNotifiedAnnouncementIds()
    fun areFabFiltersEnabled(): Flow<Boolean>
    suspend fun setAreFabFiltersEnabled(value: Boolean)
}

internal class PreferencesLocalDataSourceImpl @Inject constructor(
    @UserPrefsDatastore private val dataStore: DataStore<UserPreferences>
) : PreferencesLocalDataSource {

    override fun getHasSkippedSignIn(): Flow<Boolean> =
        dataStore.data.map { it.hasSkippedSignIn }

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setHasSkippedSignIn(hasSkippedSignIn)
                .build()
        }
    }

    override fun getUserTheme(): Flow<Theme> =
        dataStore.data.map { it.theme }


    override suspend fun setUserTheme(theme: Theme) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setTheme(theme)
                .build()
        }
    }

    override fun getDynamicColor(): Flow<Boolean> =
        dataStore.data.map { it.enableDynamicColor }


    override suspend fun setDynamicColor(value: Boolean) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setEnableDynamicColor(value)
                .build()
        }
    }

    override fun getSortType(): Flow<Sort> =
        dataStore.data.map { it.sortType }

    override suspend fun setSortType(sortType: Sort) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setSortType(sortType)
                .build()
        }
    }

    override fun getSearchScope(): Flow<QueryScope> =
        dataStore.data.map { it.queryScope }


    override suspend fun setSearchScope(queryScope: QueryScope) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setQueryScope(queryScope)
                .build()
        }
    }

    override fun getEnableForYou(): Flow<Boolean> =
        dataStore.data.map { it.enableForYou }

    override suspend fun setEnableForYou(value: Boolean) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setEnableForYou(value)
                .build()
        }
    }

    override fun getNotifiedAnnouncementIds(): Flow<List<Int>> =
        dataStore.data.map { it.notifiedAnnouncementIdsList }

    override suspend fun setNotifiedAnnouncementId(notificationId: Int) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .addNotifiedAnnouncementIds(notificationId)
                .build()
        }
    }

    override suspend fun setNotifiedAnnouncementId(notificationIds: List<Int>) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .clearNotifiedAnnouncementIds()
                .addAllNotifiedAnnouncementIds(notificationIds)
                .build()
        }
    }

    override suspend fun clearNotifiedAnnouncementIds() {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .clearNotifiedAnnouncementIds()
                .build()
        }
    }

    override fun areFabFiltersEnabled(): Flow<Boolean> =
        dataStore.data.map { it.enableFabFilters }

    override suspend fun setAreFabFiltersEnabled(value: Boolean) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setEnableFabFilters(value)
                .build()
        }
    }
}