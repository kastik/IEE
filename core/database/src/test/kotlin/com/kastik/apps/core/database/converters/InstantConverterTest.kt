package com.kastik.apps.core.database.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.time.Instant

class InstantConverterTest {

    private val converter = InstantConverter()

    @Test
    fun fromTimestampConvertsToInstant() {
        val timestamp = 1672531200000L
        val result = converter.fromTimestamp(timestamp)
        assertThat(result).isEqualTo(Instant.fromEpochMilliseconds(timestamp))
    }

    @Test
    fun dateToTimestampConvertsToLong() {
        val timestamp = 1672531200000L
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val result = converter.dateToTimestamp(instant)
        assertThat(result).isEqualTo(timestamp)
    }

    @Test
    fun verifyLongRoundTrip() {
        val initialData = 1672531200000L
        val instant = converter.fromTimestamp(initialData)
        val result = converter.dateToTimestamp(instant)
        assertThat(result).isEqualTo(initialData)
    }

    @Test
    fun verifyInstantRoundTrip() {
        val initialData = Instant.fromEpochMilliseconds(1672531200000L)
        val timestamp = converter.dateToTimestamp(initialData)
        val result = converter.fromTimestamp(timestamp)
        assertThat(result).isEqualTo(initialData)
    }

    @Test
    fun verifyNullTimestampConvertsToNull() {
        val result = converter.fromTimestamp(null)
        assertThat(result).isNull()
    }

    @Test
    fun verifyNullInstantConvertsToNull() {
        val result = converter.dateToTimestamp(null)
        assertThat(result).isNull()
    }
}
