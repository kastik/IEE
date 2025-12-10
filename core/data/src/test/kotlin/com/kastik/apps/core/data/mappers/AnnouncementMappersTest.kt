package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.model.AnnouncementWithBody
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import com.kastik.apps.core.testing.testdata.testAnnouncementDtoList
import com.kastik.apps.core.testing.testdata.testAnnouncementEntityWrapperList
import org.junit.Test
import kotlin.test.assertEquals

internal class AnnouncementMappersTest {

    @Test
    fun announcementDtoMapsToRoomEntitiesWrapperTest() {
        testAnnouncementDtoList.forEach { announcementDto ->
            val entity = announcementDto.toRoomEntities()

            assertEquals(announcementDto.id, entity.announcement.id)
            assertEquals(announcementDto.title, entity.announcement.title)
            assertEquals(announcementDto.author.id, entity.author.id)

            assertEquals(announcementDto.tags.size, entity.tags.size)

            val dtoTagIds = announcementDto.tags.map { it.id }.sorted()
            val entityTagIds = entity.tags.map { it.id }.sorted()
            assertEquals(dtoTagIds, entityTagIds)


            val crossRefAnnouncementIds = entity.tagCrossRefs.map { it.tagId }.distinct()
            assertEquals(dtoTagIds, crossRefAnnouncementIds)

            val crossRefTagIds = entity.tagCrossRefs.map { it.tagId }.sorted()
            assertEquals(dtoTagIds, crossRefTagIds)

            assertEquals(
                announcementDto.attachments.firstOrNull()?.id, entity.attachments.firstOrNull()?.id
            )
        }
    }

    @Test
    fun announcementWithoutBodyMapsToDomainTest() {
        testAnnouncementEntityWrapperList.forEach { wrapper ->
            val announcementDatabaseView = AnnouncementWithoutBody(
                announcement = wrapper.announcement,
                author = wrapper.author,
                tags = wrapper.tags,
                attachments = wrapper.attachments
            )
            val announcementViewDomain = announcementDatabaseView.toAnnouncementPreview()
            assertEquals(wrapper.announcement.id, announcementViewDomain.id)
            assertEquals(wrapper.announcement.title, announcementViewDomain.title)
            assertEquals(
                wrapper.attachments.firstOrNull()?.id,
                announcementViewDomain.attachments.firstOrNull()?.id
            )
            assertEquals(
                wrapper.tags.firstOrNull()?.id, announcementViewDomain.tags.firstOrNull()?.id
            )
        }
    }

    @Test
    fun announcementWithBodyMapsToDomainTest() {
        testAnnouncementEntityWrapperList.forEach { wrapper ->
            val announcementDatabaseView = AnnouncementWithBody(
                announcement = wrapper.announcement,
                body = wrapper.body,
                author = wrapper.author,
                tags = wrapper.tags,
                attachments = wrapper.attachments
            )
            val announcementViewDomain = announcementDatabaseView.toAnnouncementView()
            assertEquals(wrapper.announcement.id, announcementViewDomain.id)
            assertEquals(wrapper.announcement.title, announcementViewDomain.title)
            assertEquals(
                wrapper.attachments.firstOrNull()?.id,
                announcementViewDomain.attachments.firstOrNull()?.id
            )
            assertEquals(
                wrapper.tags.firstOrNull()?.id, announcementViewDomain.tags.firstOrNull()?.id
            )
            assertEquals(wrapper.body.body.length, announcementViewDomain.body.length)
        }
    }
}


