package com.kastik.apps.core.datastore.migrations

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object RemoveExpirationKey : DataMigration<Preferences> {
    private val EXPIRATION_KEY = intPreferencesKey("aboard_access_token_expiration")

    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        return currentData.contains(EXPIRATION_KEY)
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val mutablePrefs = currentData.toMutablePreferences()
        mutablePrefs.remove(EXPIRATION_KEY)
        return mutablePrefs.toPreferences()
    }

    override suspend fun cleanUp() {}
}

object RemoveRefreshKey : DataMigration<Preferences> {
    private val REFRESH_TIME_KEY =
        longPreferencesKey("aboard_access_token_last_refresh_time")

    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        return currentData.contains(REFRESH_TIME_KEY)
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val mutablePrefs = currentData.toMutablePreferences()
        mutablePrefs.remove(REFRESH_TIME_KEY)
        return mutablePrefs.toPreferences()
    }

    override suspend fun cleanUp() {}
}