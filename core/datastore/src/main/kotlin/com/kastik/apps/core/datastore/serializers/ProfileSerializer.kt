package com.kastik.apps.core.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.kastik.apps.core.datastore.proto.ProfileProto
import java.io.InputStream
import java.io.OutputStream

object ProfileSerializer : Serializer<ProfileProto> {
    override val defaultValue: ProfileProto = ProfileProto.newBuilder()
        .build()

    override suspend fun readFrom(input: InputStream): ProfileProto {
        try {
            return ProfileProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ProfileProto, output: OutputStream) =
        t.writeTo(output)
}

