package com.kastik.apps.core.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.kastik.apps.core.datastore.proto.OnboardStageProto
import com.kastik.apps.core.datastore.proto.StageProto
import java.io.InputStream
import java.io.OutputStream

object OnboardSerializer : Serializer<OnboardStageProto> {
    override val defaultValue: OnboardStageProto =
        OnboardStageProto.newBuilder()
            .setHasFinishedOnboard(false)
            .setCurrentStage(StageProto.Welcome)
            .build()

    override suspend fun readFrom(input: InputStream): OnboardStageProto {
        try {
            return OnboardStageProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: OnboardStageProto, output: OutputStream) = t.writeTo(output)
}
