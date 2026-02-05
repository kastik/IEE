package com.kastik.apps.core.datastore.serializers

import androidx.datastore.core.Serializer
import com.kastik.apps.core.datastore.proto.UserPreferences
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.newBuilder()
        .setEnableDynamicColor(true)
        .setEnableForYou(true)
        .build()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        return UserPreferences.parseFrom(input)
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
        t.writeTo(output)
}