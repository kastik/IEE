package com.kastik.apps.core.datastore.serializers

import androidx.datastore.core.Serializer
import com.kastik.apps.core.datastore.proto.SubscribedTagsProto
import java.io.InputStream
import java.io.OutputStream

object SubscribedTagsSerializer : Serializer<SubscribedTagsProto> {
    override val defaultValue: SubscribedTagsProto = SubscribedTagsProto.newBuilder()
        .build()

    override suspend fun readFrom(input: InputStream): SubscribedTagsProto {
        return SubscribedTagsProto.parseFrom(input)
    }

    override suspend fun writeTo(t: SubscribedTagsProto, output: OutputStream) =
        t.writeTo(output)
}