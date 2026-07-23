package com.kastik.apps.core.network.serializers

import com.google.common.truth.Truth.assertThat
import kotlin.time.Instant
import kotlinx.serialization.json.Json
import org.junit.Test

class StringToInstantSerializerTest {

    private val json = Json

    @Test
    fun serverNonStandardFormatDeserializesTest() {
        val input = "\"2026-08-21 00:00\""
        val result = json.decodeFromString(StringToInstantSerializer, input)

        assertThat(result).isEqualTo(Instant.parse("2026-08-21T00:00:00Z"))
    }
}
