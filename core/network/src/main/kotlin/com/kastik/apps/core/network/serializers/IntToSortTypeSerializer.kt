package com.kastik.apps.core.network.serializers

import com.kastik.apps.core.model.aboard.SortType
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class SortTypeQueryConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, String>? {
        if (type == SortType::class.java) {
            return Converter<SortType, String> { sortType ->
                when (sortType) {
                    SortType.Priority -> "0"
                    SortType.DESC -> "1"
                    SortType.ASC -> "2"
                }
            }
        }
        return null
    }
}
