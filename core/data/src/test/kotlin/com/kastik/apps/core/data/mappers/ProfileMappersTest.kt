package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.datastore.testdata.userProfileProtoTestData
import com.kastik.apps.core.network.testdata.userProfileDtoTestData
import org.junit.Test

class ProfileMappersTest {

    @Test
    fun userProfileDtoMapsToProfileProto() {
        val dto = userProfileDtoTestData
        val proto = dto.map { it.toProfileProto() }

        assertThat(proto).isNotEmpty()
        assertThat(proto.size).isEqualTo(dto.size)
        assertThat(proto.map { it.name }).containsExactlyElementsIn(dto.map { it.name })
    }

    @Test
    fun userProfileDtoMapsToProfileProtoWithEmptyName() {
        val dto = userProfileDtoTestData.map { it.copy(name = "") }
        val proto = dto.map { it.toProfileProto() }

        assertThat(proto).isNotEmpty()
        assertThat(proto.map { it.name }).containsExactlyElementsIn(dto.map { it.nameEng })
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