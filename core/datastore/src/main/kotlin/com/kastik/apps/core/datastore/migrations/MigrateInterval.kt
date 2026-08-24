package com.kastik.apps.core.datastore.migrations

import androidx.datastore.core.DataMigration
import com.kastik.apps.core.datastore.proto.UserPreferencesProto

object MigrateInterval : DataMigration<UserPreferencesProto> {

    const val DEFAULT_CHECK_INTERVAL_MINUTES = 120

    override suspend fun shouldMigrate(currentData: UserPreferencesProto) =
        currentData.checkIntervalMinutes == 0

    override suspend fun migrate(currentData: UserPreferencesProto): UserPreferencesProto =
        currentData.toBuilder().setCheckIntervalMinutes(DEFAULT_CHECK_INTERVAL_MINUTES).build()

    override suspend fun cleanUp() = Unit
}
