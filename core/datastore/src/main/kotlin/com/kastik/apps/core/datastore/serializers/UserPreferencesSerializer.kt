package com.kastik.apps.core.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.kastik.apps.core.datastore.proto.SearchScopeProto
import com.kastik.apps.core.datastore.proto.SortTypeProto
import com.kastik.apps.core.datastore.proto.ThemeProto
import com.kastik.apps.core.datastore.proto.UserPreferencesProto
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferencesProto> {
    override val defaultValue: UserPreferencesProto =
        UserPreferencesProto.newBuilder()
            .setTheme(ThemeProto.System)
            .setIsForYouEnabled(true)
            .setAreFabFiltersEnabled(false)
            .setIsDynamicColorEnabled(true)
            .setSortType(SortTypeProto.Priority)
            .setSearchScope(SearchScopeProto.Body)
            .setHasSkippedSignIn(false)
            .setCheckIntervalMinutes(120)
            .build()

    override suspend fun readFrom(input: InputStream): UserPreferencesProto {
        try {
            return UserPreferencesProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferencesProto, output: OutputStream) = t.writeTo(output)
}
