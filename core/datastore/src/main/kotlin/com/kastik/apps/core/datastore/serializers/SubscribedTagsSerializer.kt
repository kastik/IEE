package com.kastik.apps.core.datastore.serializers

import androidx.datastore.core.Serializer
import com.kastik.apps.core.datastore.proto.SubscriptionsProto
import java.io.InputStream
import java.io.OutputStream

object SubscribedTagsSerializer : Serializer<SubscriptionsProto> {
    override val defaultValue: SubscriptionsProto = SubscriptionsProto.newBuilder()
        .build()

    override suspend fun readFrom(input: InputStream): SubscriptionsProto {
        return SubscriptionsProto.parseFrom(input)
    }

    override suspend fun writeTo(t: SubscriptionsProto, output: OutputStream) =
        t.writeTo(output)
}