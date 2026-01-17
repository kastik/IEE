package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.testdata.userProfileDtoTestData
import com.kastik.apps.core.testing.testdata.userProfileProtoTestData
import org.junit.Test
import kotlin.test.assertEquals

class ProfileMappersTest {

    @Test
    fun userProfileDtoMapsToProfileProto() {
        val dto = userProfileDtoTestData
        val proto = dto.map { it.toProfileProto() }

        assertThat(proto).isNotEmpty()
        assertThat(proto.size).isEqualTo(dto.size)

        dto.zip(proto).forEach { (dto, proto) ->
            assertThat(proto.name).isEqualTo(dto.nameEng)
            assertThat(proto.id).isEqualTo(dto.id)
            assertThat(proto.email).isEqualTo(dto.email)
            assertThat(proto.deletedAt).isEqualTo(dto.deletedAt)
        }
    }

    @Test
    fun userProfileDtoMapsToProfileProtoWithEmptyName() {
        val dto = userProfileDtoTestData.map { it.copy(name = "") }
        val proto = dto.map { it.toProfileProto() }

        assertThat(proto).isNotEmpty()
        assertThat(proto.size).isEqualTo(dto.size)

        dto.zip(proto).forEach { (dto, proto) ->
            assertThat(proto.name).isEqualTo(dto.name.ifEmpty { dto.nameEng })
            assertThat(proto.id).isEqualTo(dto.id)
            assertThat(proto.email).isEqualTo(dto.email)
            assertThat(proto.deletedAt).isEqualTo(dto.deletedAt)
        }
    }

    @Test
    fun profileProtoMapsToProfile() {
        val proto = userProfileProtoTestData
        val domain = proto.map { it.toProfile() }

        assertThat(domain).isNotEmpty()
        assertThat(domain.size).isEqualTo(proto.size)

        proto.zip(domain).forEach { (proto, domain) ->
            assertThat(domain.id).isEqualTo(proto.id)
            assertThat(domain.name).isEqualTo(proto.name)
            assertThat(domain.email).isEqualTo(proto.email)
            assertThat(domain.deletedAt).isEqualTo(proto.deletedAt)
        }
    }
}