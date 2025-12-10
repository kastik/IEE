package com.kastik.apps.core.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.kastik.apps.core.datastore.proto.SubscribableTagsProto
import java.io.InputStream
import java.io.OutputStream

object SubscribableTagsSerializer : Serializer<SubscribableTagsProto> {
    override val defaultValue: SubscribableTagsProto = SubscribableTagsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SubscribableTagsProto {
        try {
            return SubscribableTagsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SubscribableTagsProto, output: OutputStream) = t.writeTo(output)
}