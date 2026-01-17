package com.kastik.apps.core.database.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ListIntConverterTest {

    private val converter = IntListConverter()

    @Test
    fun fromStringConvertsToIntList() {
        val result = converter.fromString("1,2,3")
        assertThat(result).containsExactly(1, 2, 3)
    }

    @Test
    fun fromListConvertsToString() {
        val result = converter.fromList(listOf(1, 2, 3))
        assertThat(result).isEqualTo("1,2,3")
    }

    @Test
    fun verifyStringRoundTrip() {
        val initialData = "1,2,3"
        val intList = converter.fromString(initialData)
        val result = converter.fromList(intList)
        assertThat(initialData).isEqualTo(result)
    }

    @Test
    fun verifyIntListRoundTrip() {
        val initialData = listOf(1, 2, 3)
        val stringList = converter.fromList(initialData)
        val result = converter.fromString(stringList)
        assertThat(initialData).isEqualTo(result)
    }

    @Test
    fun verifyEmptyListRoundTrip() {
        val initialData = emptyList<Int>()
        val stringList = converter.fromList(initialData)
        val result = converter.fromString(stringList)
        assertThat(initialData).isEqualTo(result)
    }

    @Test
    fun verifyNullListCovertsToEmptyString() {
        val result = converter.fromList(null)
        assertThat(result).isEmpty()
    }

    @Test
    fun verifyNullStringCovertsToEmptyList() {
        val result = converter.fromString(null)
        assertThat(result).isEmpty()
    }
}