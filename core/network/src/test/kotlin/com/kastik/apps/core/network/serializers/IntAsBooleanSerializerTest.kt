package com.kastik.apps.core.network.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test

class IntAsBooleanSerializerTest {

    private val json = Json

    @Test
    fun oneDeserializesToTrueTest() {
        val result = json.decodeFromString(IntAsBooleanSerializer, "1")
        assertThat(result).isTrue()
    }

    @Test
    fun zeroDeserializesToFalseTest() {
        val result = json.decodeFromString(IntAsBooleanSerializer, "0")
        assertThat(result).isFalse()
    }

    @Test
    fun nullDeserializesToFalseTest() {
        val result = json.decodeFromString(IntAsBooleanSerializer, "null")
        assertThat(result).isFalse()
    }
}
