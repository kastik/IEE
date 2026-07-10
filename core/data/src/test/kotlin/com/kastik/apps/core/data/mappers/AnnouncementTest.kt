package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.network.testdata.baseAnnouncementDto
import org.junit.Test

internal class AnnouncementTest {

    @Test
    fun announcementDtoMapsToAnnouncementEntityTest() {
        val announcementDto = baseAnnouncementDto
        val result = baseAnnouncementDto.toAnnouncementEntity()

        assertThat(result.id).isEqualTo(announcementDto.id)
        assertThat(result.title).isEqualTo(announcementDto.title)
        assertThat(result.authorId).isEqualTo(announcementDto.author.id)
    }

    @Test
    fun announcementDtoMapsToBodyEntityTest() {
        val announcementDto = baseAnnouncementDto
        val result = baseAnnouncementDto.toBodyEntity()

        assertThat(result).isNotNull()
        assertThat(result.announcementId).isEqualTo(announcementDto.id)
        assertThat(result.body).isEqualTo(announcementDto.body)
    }

    @Test
    fun announcementDtoMapsToAuthorEntityTest() {
        val announcementDto = baseAnnouncementDto
        val result = baseAnnouncementDto.author.toAuthorEntity()

        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(announcementDto.author.id)
        assertThat(result.name).isEqualTo(announcementDto.author.name)
    }

    @Test
    fun announcementDtoMapsToTagEntityTest() {
        val announcementDto = baseAnnouncementDto
        val tagEntity = baseAnnouncementDto.tags.map { it.toTagEntity() }

        tagEntity.forEachIndexed { index, tag ->
            assertThat(tag.id).isEqualTo(announcementDto.tags[index].id)
            assertThat(tag.title).isEqualTo(announcementDto.tags[index].title)
            assertThat(tag.parentId).isEqualTo(announcementDto.tags[index].parentId)
            assertThat(tag.isPublic).isEqualTo(announcementDto.tags[index].isPublic)
        }
    }

    @Test
    fun announcementDtoMapsToTagsCrossRefEntityTest() {
        val announcementDto = baseAnnouncementDto
        val tagsCrossRefEntities = announcementDto.toTagCrossRefs()

        assertThat(tagsCrossRefEntities).isNotNull()
        assertThat(tagsCrossRefEntities.size).isEqualTo(announcementDto.tags.size)
        tagsCrossRefEntities.forEachIndexed { index, tag ->
            assertThat(tag.tagId).isEqualTo(announcementDto.tags[index].id)
        }
    }

    @Test
    fun announcementDtoMapsToAttachmentEntityTest() {
        val announcementDto = baseAnnouncementDto
        val attachmentEntities = announcementDto.attachments.map { it.toAttachmentEntity() }

        attachmentEntities.forEachIndexed { index, attachment ->
            assertThat(attachment.id).isEqualTo(announcementDto.attachments[index].id)
            assertThat(attachment.announcementId).isEqualTo(announcementDto.attachments[index].announcementId)
            assertThat(attachment.filename).isEqualTo(announcementDto.attachments[index].fileName)
        }
    }
}


