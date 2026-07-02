package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.datastore.testdata.baseProfileProto
import com.kastik.apps.core.network.testdata.baseProfileDto
import org.junit.Test

class ProfileTest {

    @Test
    fun userProfileDtoMapsToProfileProto() {

        val profileDto = baseProfileDto
        val result = profileDto.toProfileProto()

        assertThat(result.id).isEqualTo(profileDto.id)
        assertThat(result.uid).isEqualTo(profileDto.uid)
        assertThat(result.name).isEqualTo(profileDto.name)
        assertThat(result.email).isEqualTo(profileDto.email)
    }

    @Test
    fun profileProtoMapsToProfile() {
        val profileProto = baseProfileProto
        val result = profileProto.toProfile()

        assertThat(result.id).isEqualTo(profileProto.id)
        assertThat(result.uid).isEqualTo(profileProto.uid)
        assertThat(result.name).isEqualTo(profileProto.name)
        assertThat(result.email).isEqualTo(profileProto.email)
    }

}