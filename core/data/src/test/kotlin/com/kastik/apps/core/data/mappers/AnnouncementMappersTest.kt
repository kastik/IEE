package com.kastik.apps.core.data.mappers

import com.google.common.truth.Truth.assertThat
import com.kastik.apps.core.testing.testdata.announcementDetailsRelationTestData
import com.kastik.apps.core.testing.testdata.announcementDtoTestData
import com.kastik.apps.core.testing.testdata.announcementPreviewRelationTestData
import org.junit.Test

internal class AnnouncementMappersTest {

    @Test
    fun announcementDtoMapsToAnnouncementEntityTest() {
        announcementDtoTestData.forEach { announcementDto ->
            val announcementEntity = announcementDto.toAnnouncementEntity()
            assertThat(announcementEntity).isNotNull()
            assertThat(announcementEntity.id).isEqualTo(announcementDto.id)
            assertThat(announcementEntity.title).isEqualTo(announcementDto.title)
            assertThat(announcementEntity.authorId).isEqualTo(announcementDto.author.id)
        }
    }

    @Test
    fun announcementDtoMapsToBodyEntityTest() {
        announcementDtoTestData.forEach { announcementDto ->
            val bodyEntity = announcementDto.toBodyEntity()
            assertThat(bodyEntity).isNotNull()
            assertThat(bodyEntity.announcementId).isEqualTo(announcementDto.id)
            assertThat(bodyEntity.body).isEqualTo(announcementDto.body)
        }
    }

    @Test
    fun announcementDtoMapsToAuthorEntityTest() {
        announcementDtoTestData.forEach { announcementDto ->
            val authorEntity = announcementDto.author.toAuthorEntity()
            assertThat(authorEntity).isNotNull()
            assertThat(authorEntity.id).isEqualTo(announcementDto.author.id)
            assertThat(authorEntity.name).isEqualTo(announcementDto.author.name)
        }
    }

    @Test
    fun announcementDtoMapsToTagEntityTest() {
        announcementDtoTestData.forEach { announcementDto ->
            val tagEntity = announcementDto.tags.map { it.toTagEntity() }
            assertThat(tagEntity).isNotNull()
            assertThat(tagEntity.size).isEqualTo(announcementDto.tags.size)
            tagEntity.forEachIndexed { index, tag ->
                assertThat(tag.id).isEqualTo(announcementDto.tags[index].id)
                assertThat(tag.title).isEqualTo(announcementDto.tags[index].title)
                assertThat(tag.parentId).isEqualTo(announcementDto.tags[index].parentId)
                assertThat(tag.isPublic).isEqualTo(announcementDto.tags[index].isPublic)
            }
        }
    }

    @Test
    fun announcementDtoMapsToTagsCrossRefEntityTest() {
        announcementDtoTestData.forEach { announcementDto ->
            val tagsCrossRefEntities = announcementDto.toTagCrossRefs()
            assertThat(tagsCrossRefEntities).isNotNull()
            assertThat(tagsCrossRefEntities.size).isEqualTo(announcementDto.tags.size)
            tagsCrossRefEntities.forEachIndexed { index, tag ->
                //TODO
                assertThat(tag.tagId).isEqualTo(announcementDto.tags[index].id)
            }
        }
    }

    @Test
    fun announcementDtoMapsToAttachmentEntityTest() {
        announcementDtoTestData.forEach { announcementDto ->
            val attachmentEntities = announcementDto.attachments.map { it.toAttachmentEntity() }
            assertThat(attachmentEntities).isNotNull()
            assertThat(attachmentEntities.size).isEqualTo(announcementDto.attachments.size)
            attachmentEntities.forEachIndexed { index, attachment ->
                assertThat(attachment.id).isEqualTo(announcementDto.attachments[index].id)
                assertThat(attachment.announcementId).isEqualTo(announcementDto.attachments[index].announcementId)
                assertThat(attachment.filename).isEqualTo(announcementDto.attachments[index].filename)
            }
        }
    }

    @Test
    fun announcementPreviewRelationMapsToDomainTest() {
        announcementPreviewRelationTestData.forEach { announcementPreview ->
            val announcementDomain = announcementPreview.toAnnouncement()
            assertThat(announcementDomain.id).isEqualTo(announcementPreview.announcement.id)
            assertThat(announcementDomain.title).isEqualTo(announcementPreview.announcement.title)
            assertThat(announcementDomain.attachments.firstOrNull()?.id).isEqualTo(
                announcementPreview.attachments.firstOrNull()?.id
            )
            assertThat(announcementDomain.tags.firstOrNull()?.id).isEqualTo(announcementPreview.tags.firstOrNull()?.id)
        }
    }

    @Test
    fun announcementDetailRelationMapsToDomainTest() {
        announcementDetailsRelationTestData.forEach { announcementDetail ->
            val announcementDomain = announcementDetail.toAnnouncement()
            assertThat(announcementDomain.id).isEqualTo(announcementDetail.announcement.id)
            assertThat(announcementDomain.title).isEqualTo(announcementDetail.announcement.title)
            assertThat(announcementDomain.attachments.firstOrNull()?.id).isEqualTo(
                announcementDetail.attachments.firstOrNull()?.id
            )
            assertThat(announcementDomain.tags.firstOrNull()?.id).isEqualTo(announcementDetail.tags.firstOrNull()?.id)
            assertThat(announcementDomain.body.length).isEqualTo(announcementDetail.body.body.length)
        }
    }
}


