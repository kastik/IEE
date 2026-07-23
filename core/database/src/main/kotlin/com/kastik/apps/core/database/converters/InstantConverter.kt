package com.kastik.apps.core.database.converters

import androidx.room.TypeConverter
import kotlin.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilliseconds()
    }
}
