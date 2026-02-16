package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.relations.AnnouncementDetailRelation
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation


internal val baseAnnouncementEntity = AnnouncementEntity(
    id = 1,
    title = "Test Announcement",
    engTitle = null,
    hasEng = false,
    preview = "Test Preview",
    engPreview = null,
    createdAt = "2025-10-24 11:44",
    updatedAt = "2025-10-24 11:44",
    isPinned = false,
    pinnedUntil = null,
    isEvent = null,
    eventStartTime = null,
    eventEndTime = null,
    eventLocation = null,
    gmaps = null,
    announcementUrl = "https://aboard.iee.ihu.gr/announcements/1",
    authorId = 1
)

internal val baseBodyEntity = BodyEntity(
    announcementId = 1,
    body = "Test Body",
    engBody = ""
)

internal val commonTagEntities = listOf(baseTagEntity)
internal val commonAttachmentEntities = listOf(baseAttachmentEntity)

val announcementEntityTestData = listOf(
    baseAnnouncementEntity,
    baseAnnouncementEntity.copy(
        id = 2,
        engTitle = "Test Announcement Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementEntity.copy(
        id = 3,
        isEvent = true,
        eventStartTime = "2025-10-25 11:44",
        eventEndTime = "2025-10-26 11:44",
        eventLocation = "IHU-Campus",
        gmaps = "https://www.google.com/maps/something"
    ),
    baseAnnouncementEntity.copy(id = 4),
    baseAnnouncementEntity.copy(id = 5),
    baseAnnouncementEntity.copy(
        id = 6,
        engTitle = "Test Announcement Eng",
        engPreview = "Test Preview Eng",
        hasEng = true,
        isEvent = true,
        eventStartTime = "2025-10-25 11:44",
        eventEndTime = "2025-10-26 11:44",
        eventLocation = "IHU-Campus",
        gmaps = "https://www.google.com/maps/something"
    ),
    baseAnnouncementEntity.copy(
        id = 7,
        engTitle = "Test Announcement Eng",
        engPreview = "Test Preview Eng",
        hasEng = true,
        isEvent = true,
        eventStartTime = "2025-10-25 11:44",
        eventEndTime = "2025-10-26 11:44",
        eventLocation = "IHU-Campus",
        gmaps = "https://www.google.com/maps/something"
    ),
    *(8..20).map { id ->
        baseAnnouncementEntity.copy(
            id = id,
            engTitle = "Test Announcement Eng",
            engPreview = "Test Preview Eng",
            hasEng = true
        )
    }.toTypedArray()
)

val announcementBodyEntityTestData = announcementEntityTestData.map { entity ->
    baseBodyEntity.copy(
        announcementId = entity.id,
        engBody = if (entity.hasEng) "Test Body Eng" else ""
    )
}

val announcementTagsCrossRefEntityTestData = announcementEntityTestData.flatMap { entity ->
    if (entity.id in listOf(5, 6, 7)) {
        commonTagEntities.map { tag ->
            TagsCrossRefEntity(announcementId = entity.id, tagId = tag.id)
        }
    } else {
        emptyList()
    }
}

val announcementPreviewRelationTestData = announcementEntityTestData.map { entity ->
    val tags = if (entity.id in listOf(5, 6, 7)) commonTagEntities else emptyList()
    val attachments = if (entity.id in listOf(4, 6, 7)) {
        commonAttachmentEntities.map { it.copy(announcementId = entity.id) }
    } else {
        emptyList()
    }

    AnnouncementPreviewRelation(
        announcement = entity,
        author = baseAuthorEntity,
        tags = tags,
        attachments = attachments
    )
}

val announcementDetailsRelationTestData = announcementEntityTestData.map { entity ->
    val tags = if (entity.id in listOf(5, 6, 7)) commonTagEntities else emptyList()
    val attachments = if (entity.id in listOf(4, 6, 7)) {
        commonAttachmentEntities.map { it.copy(announcementId = entity.id) }
    } else {
        emptyList()
    }

    AnnouncementDetailRelation(
        announcement = entity,
        body = baseBodyEntity.copy(
            announcementId = entity.id,
            engBody = if (entity.hasEng) "Test Body Eng" else ""
        ),
        author = baseAuthorEntity,
        tags = tags,
        attachments = attachments,
    )
}

val tagEntitiesTestData = commonTagEntities
val authorEntitiesTestData = listOf(baseAuthorEntity)