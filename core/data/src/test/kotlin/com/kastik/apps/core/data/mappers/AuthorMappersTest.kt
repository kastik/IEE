package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.testdata.announcementAuthorEntityTestData
import com.kastik.apps.core.testing.testdata.authorDtoTestData
import org.junit.Test


class AuthorMappersTest {

    @Test
    fun announcementAuthorDtoMapsToAuthorEntity() {
        val dto = authorDtoTestData
        val entity = dto.map { it.toAuthorEntity() }

        assertThat(entity).isNotEmpty()
        assertThat(entity.size).isEqualTo(dto.size)

        dto.zip(entity).forEach { (dto, entity) ->
            assertThat(entity.id).isEqualTo(dto.id)
            assertThat(entity.name).isEqualTo(dto.name)
            assertThat(entity.announcementCount).isEqualTo(dto.announcementCount)
        }
    }

    @Test
    fun authorEntityMapsToAuthor() {
        val entity = announcementAuthorEntityTestData
        val domain = entity.map { it.toAuthor() }

        assertThat(domain).isNotEmpty()
        assertThat(domain.size).isEqualTo(entity.size)

        entity.zip(entity).forEach { (entity, domain) ->
            assertThat(domain.id).isEqualTo(entity.id)
            assertThat(domain.name).isEqualTo(entity.name)
            assertThat(domain.announcementCount).isEqualTo(entity.announcementCount)
        }
    }
}