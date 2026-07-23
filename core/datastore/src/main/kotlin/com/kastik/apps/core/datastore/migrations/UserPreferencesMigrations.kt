package com.kastik.apps.core.datastore.migrations

import androidx.datastore.core.DataMigration
import com.kastik.apps.core.datastore.proto.UserPreferencesProto

object MigrateInterval : DataMigration<UserPreferencesProto> {

    override suspend fun shouldMigrate(currentData: UserPreferencesProto) =
        currentData.checkIntervalMinutes == 0

    override suspend fun migrate(currentData: UserPreferencesProto) =
        currentData.toBuilder().setCheckIntervalMinutes(120).build()

    override suspend fun cleanUp() {}
}
