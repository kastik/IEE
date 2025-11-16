package com.kastik.apps.core.network.serializers

import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class IntAsBooleanSerializerTest {

    private val json = Json

    @Test
    fun oneDeserializesToTrueTest() {
        assertEquals(true, json.decodeFromString(IntAsBooleanSerializer, "1"))
    }

    @Test
    fun zeroDeserializesToFalseTest() {
        assertEquals(false, json.decodeFromString(IntAsBooleanSerializer, "0"))
    }

    @Test
    fun nullDeserializesToTrueTest() {
        assertEquals(false, json.decodeFromString(IntAsBooleanSerializer, "null"))
    }

}
