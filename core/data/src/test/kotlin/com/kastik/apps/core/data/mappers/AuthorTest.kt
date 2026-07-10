package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.network.testdata.baseAuthorDto
import com.kastik.apps.core.testing.testdata.baseAuthorEntity
import org.junit.Test


class AuthorTest {

    @Test
    fun announcementAuthorDtoMapsToAuthorEntity() {
        val dto = baseAuthorDto
        val entity = dto.toAuthorEntity()

        assertThat(entity.id).isEqualTo(dto.id)
        assertThat(entity.name).isEqualTo(dto.name)
        assertThat(entity.announcementCount).isEqualTo(dto.announcementCount)
    }

    @Test
    fun authorEntityMapsToAuthor() {
        val entity = baseAuthorEntity
        val domain = entity.toAuthor()

        assertThat(domain.id).isEqualTo(entity.id)
        assertThat(domain.name).isEqualTo(entity.name)
        assertThat(domain.announcementCount).isEqualTo(entity.announcementCount)
    }
}