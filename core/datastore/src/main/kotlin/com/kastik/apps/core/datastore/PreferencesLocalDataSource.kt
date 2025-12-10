package com.kastik.apps.core.datastore

import androidx.datastore.core.DataStore
import com.kastik.apps.core.datastore.proto.Theme
import com.kastik.apps.core.datastore.proto.UserPreferences
import com.kastik.apps.core.di.UserPrefsDatastore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferencesLocalDataSource {
    fun getHasSkippedSignIn(): Flow<Boolean>
    suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean)
    fun getUserTheme(): Flow<Theme>
    suspend fun setUserTheme(value: Theme)
    fun getDynamicColor(): Flow<Boolean>
    suspend fun setDynamicColor(value: Boolean)

}

internal class PreferencesLocalDataSourceImpl(
    @param:UserPrefsDatastore private val dataStore: DataStore<UserPreferences>
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


    override suspend fun setUserTheme(value: Theme) {
        dataStore.updateData { prefs ->
            prefs.toBuilder()
                .setTheme(value)
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
}