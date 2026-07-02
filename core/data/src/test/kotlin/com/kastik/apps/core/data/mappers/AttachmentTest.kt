package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.network.testdata.baseAttachmentDto
import com.kastik.apps.core.testing.testdata.baseAttachmentEntity
import org.junit.Test
import kotlin.test.assertEquals

class AttachmentTest {

    @Test
    fun attachmentDtoMapsToAttachmentEntityTest() {
        val dto = baseAttachmentDto
        val entity = dto.toAttachmentEntity()

        assertEquals(dto.id, entity.id)
        assertEquals(dto.announcementId, entity.announcementId)
        assertEquals(dto.fileName, entity.filename)
    }

    @Test
    fun attachmentEntityMapsToAttachmentTest() {
        val entity = baseAttachmentEntity
        val domain = entity.toAttachment()

        assertEquals(entity.id, domain.id)
        assertEquals(entity.filename, domain.fileName)

    }
}