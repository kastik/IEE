package com.kastik.apps.core.data.mappers

import org.junit.Test
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.testing.testdata.attachmentDtoTestData
import com.kastik.apps.core.testing.testdata.attachmentEntityTestData
import kotlin.test.assertEquals

class AttachmentMappersTest {

    @Test
    fun attachmentDtoMapsToAttachmentEntityTest() {
        val dto = attachmentDtoTestData
        val entity = dto.map { it.toAttachmentEntity() }

        assertEquals(dto.size, entity.size)

        dto.zip(entity).forEach { (dto, entity) ->
            assertEquals(dto.id, entity.id)
            assertEquals(dto.announcementId, entity.announcementId)
            assertEquals(dto.filename, entity.filename)
        }
    }

    @Test
    fun attachmentEntityMapsToAttachmentTest() {
        val entity = attachmentEntityTestData
        val domain = entity.map { it.toAttachment() }

        assertEquals(entity.size, domain.size)

        entity.zip(entity).forEach { (entity, domain) ->
            assertEquals(entity.id, domain.id)
            assertEquals(entity.announcementId, domain.announcementId)
            assertEquals(entity.filename, domain.filename)
        }
    }
}