package com.kastik.apps.core.network.serializers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.model.aboard.SortType
import org.junit.Test
import retrofit2.Converter
import retrofit2.Retrofit

class IntToSortTypeSerializerTest {

    private val factory = SortTypeQueryConverterFactory()
    private val dummyRetrofit = Retrofit.Builder().baseUrl("http://localhost/").build()
    private val emptyAnnotations = emptyArray<Annotation>()

    @Test
    fun priorityConvertsToZeroTest() {
        // Explicitly cast to Converter<SortType, String>
        val converter = factory.stringConverter(
            SortType::class.java,
            emptyAnnotations,
            dummyRetrofit
        ) as Converter<SortType, String>?

        val result = converter?.convert(SortType.Priority)

        assertThat(result).isEqualTo("0")
    }

    @Test
    fun descConvertsToOneTest() {
        val converter = factory.stringConverter(
            SortType::class.java,
            emptyAnnotations,
            dummyRetrofit
        ) as Converter<SortType, String>?

        val result = converter?.convert(SortType.DESC)

        assertThat(result).isEqualTo("1")
    }

    @Test
    fun ascConvertsToTwoTest() {
        val converter = factory.stringConverter(
            SortType::class.java,
            emptyAnnotations,
            dummyRetrofit
        ) as Converter<SortType, String>?

        val result = converter?.convert(SortType.ASC)

        assertThat(result).isEqualTo("2")
    }

    @Test
    fun unsupportedTypeReturnsNullTest() {
        val converter = factory.stringConverter(
            String::class.java,
            emptyAnnotations,
            dummyRetrofit
        ) as Converter<SortType, String>?

        assertThat(converter).isNull()
    }
}