package com.kastik.apps.core.database.converters

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.SortType
import org.junit.Test

class SortTypeConverterTest {

    private val converter = SortTypeConverter()

    @Test
    fun mapsSortTypeToName() {
        SortType.entries.forEach { type ->
            val result = converter.fromSortType(type)
            assertThat(result).isEqualTo(type.name)
        }
    }

    @Test
    fun mapsNameToSortType() {
        SortType.entries.forEach { type ->
            val result = converter.toSortType(type.name)
            assertThat(result).isEqualTo(type)
        }
    }
}
