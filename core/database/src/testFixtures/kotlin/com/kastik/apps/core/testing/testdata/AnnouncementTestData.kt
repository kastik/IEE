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
    baseAnnouncementEntity.copy(
        id = 4
    ),
    baseAnnouncementEntity.copy(
        id = 5
    ),
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
    baseAnnouncementEntity.copy(
        id = 8,
    ),
    baseAnnouncementEntity.copy(
        id = 9,
    ),
    baseAnnouncementEntity.copy(
        id = 10,
    )
)

internal val baseBodyEntity = BodyEntity(
    announcementId = 1,
    body = "Test Body",
    engBody = ""
)

val announcementBodyEntityTestData = announcementEntityTestData.map { entity ->
    baseBodyEntity.copy(
        announcementId = entity.id,
    )
}

val announcementTagsCrossRefEntityTestData = listOf(
    TagsCrossRefEntity(announcementId = 1, tagId = 1),
    TagsCrossRefEntity(announcementId = 1, tagId = 2),
    TagsCrossRefEntity(announcementId = 2, tagId = 1),
    TagsCrossRefEntity(announcementId = 2, tagId = 2),
    TagsCrossRefEntity(announcementId = 3, tagId = 1),
    TagsCrossRefEntity(announcementId = 3, tagId = 2),
    TagsCrossRefEntity(announcementId = 4, tagId = 1),
    TagsCrossRefEntity(announcementId = 4, tagId = 2),
    TagsCrossRefEntity(announcementId = 5, tagId = 1),
    TagsCrossRefEntity(announcementId = 5, tagId = 2)
)

val announcementPreviewRelationTestData = announcementEntityTestData.map { entity ->
    AnnouncementPreviewRelation(
        announcement = entity,
        author = baseAuthorEntity,
        tags = announcementTagEntityTestData,
        attachments = attachmentEntityTestData
    )
}

val announcementDetailsRelationTestData = announcementEntityTestData.map { entity ->
    AnnouncementDetailRelation(
        announcement = entity,
        body = baseBodyEntity.copy(
            announcementId = entity.id
        ),
        author = baseAuthorEntity,
        tags = announcementTagEntityTestData,
        attachments = attachmentEntityTestData,
    )
}