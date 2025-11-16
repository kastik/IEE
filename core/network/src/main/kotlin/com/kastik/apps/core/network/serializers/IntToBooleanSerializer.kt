package com.kastik.apps.core.network.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object IntAsBooleanSerializer : KSerializer<Boolean> {

    private val intSerializer = Int.serializer().nullable

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IntAsBoolean", PrimitiveKind.INT)

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Boolean {
        val value = decoder.decodeNullableSerializableValue(intSerializer)
        return value == 1
    }

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeInt(if (value) 1 else 0)
    }
}
