package com.kastik.apps.core.network.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test

class IntToBooleanSerializerTest {

    private val json = Json

    @Test
    fun oneDeserializesToTrueTest() {
        val result = json.decodeFromString(IntToBooleanSerializer, "1")
        assertThat(result).isTrue()
    }

    @Test
    fun zeroDeserializesToFalseTest() {
        val result = json.decodeFromString(IntToBooleanSerializer, "0")
        assertThat(result).isFalse()
    }

    @Test
    fun nullDeserializesToFalseTest() {
        val result = json.decodeFromString(IntToBooleanSerializer, "null")
        assertThat(result).isFalse()
    }
}
