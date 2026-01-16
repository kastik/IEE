package com.kastik.apps.core.database.converters

import androidx.room.TypeConverter


class IntListConverter {
    @TypeConverter
    fun fromList(list: List<Int>?): String {
        if (list.isNullOrEmpty()) return ""
        return list.sorted().joinToString(separator = ",")
    }

    @TypeConverter
    fun fromString(value: String?): List<Int> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split(",").map { it.toInt() }
    }

}