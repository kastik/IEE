package com.kastik.apps.core.database.converters

import androidx.room.TypeConverter
import com.kastik.apps.core.model.aboard.SortType

class SortTypeConverter {
    @TypeConverter
    fun fromSortType(value: SortType): String {
        return value.name
    }

    @TypeConverter
    fun toSortType(value: String): SortType {
        return SortType.valueOf(value)
    }
}
