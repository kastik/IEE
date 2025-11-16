package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.model.AnnouncementEntityWrapper

val testAuthorEntity = AuthorEntity(
    id = 1,
    name = "Kostas"
)

val testTagEntity = TagEntity(
    id = 1,
    title = "tag",
    parentId = null,
    isPublic = false,
    mailListName = "no-mail"
)

val testAttachmentEntity = AttachmentEntity(
    id = 1,
    announcementId = 1,
    filename = "file.pdf",
    fileSize = 2000,
    mimeType = "application/pdf",
    attachmentUrl = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
    attachmentUrlPreview = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1"
)

val testTagCrossRef = TagsCrossRefEntity(
    announcementId = 1,
    tagId = 1
)

val testBody = BodyEntity(
    announcementId = 1,
    body = "Test Body",
    engBody = null ?: ""
)

val testAnnouncementEntity = AnnouncementEntity(
    id = 1,
    title = "Test Announcement",
    engTitle = null,
    preview = "Test Preview",
    engPreview = null,
    hasEng = false,
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
    authorId = 1,
)


val testAnnouncementEntityEng = testAnnouncementEntity.copy(
    id = 2,
    engTitle = "Test Announcement Eng",
    engPreview = "Test Preview Eng",
    hasEng = true
)

val testAnnouncementEntityEvent = testAnnouncementEntity.copy(
    id = 3,
    isEvent = true,
    eventStartTime = "2025-10-25 11:44",
    eventEndTime = "2025-10-26 11:44",
    eventLocation = "IHU-Campus",
    gmaps = "https://www.google.com/maps/something"
)

val testAnnouncementEntityWithAttachments = testAnnouncementEntity.copy(
    id = 4
)

val testAttachmentsFor4 = listOf(
    testAttachmentEntity.copy(id = 1, announcementId = 4),
    testAttachmentEntity.copy(id = 2, announcementId = 4),
    testAttachmentEntity.copy(id = 3, announcementId = 4)
)

val testAnnouncementEntityWithTags = testAnnouncementEntity.copy(
    id = 5
)

val testTagsFor5 = listOf(
    testTagEntity,
    testTagEntity.copy(id = 2, title = "tag2"),
    testTagEntity.copy(id = 3, title = "tag3")
)

val testTagRefsFor5 = testTagsFor5.map { tag ->
    TagsCrossRefEntity(announcementId = 5, tagId = tag.id)
}

val testAnnouncementEntityCombined = testAnnouncementEntity.copy(
    id = 6,
    engTitle = "Test Announcement Eng",
    engPreview = "Test Preview Eng",
    hasEng = true,
    isEvent = true,
    eventStartTime = "2025-10-25 11:44",
    eventEndTime = "2025-10-26 11:44",
    eventLocation = "IHU-Campus",
    gmaps = "https://www.google.com/maps/something"
)

val testTagsFor6 = testTagsFor5 // same 3 tags
val testTagRefsFor6 = testTagsFor6.map { tag ->
    TagsCrossRefEntity(announcementId = 6, tagId = tag.id)
}

val testAttachmentsFor6 = listOf(
    testAttachmentEntity.copy(id = 4, announcementId = 6),
    testAttachmentEntity.copy(id = 5, announcementId = 6),
    testAttachmentEntity.copy(id = 6, announcementId = 6)
)

val testAnnouncementWrapper = AnnouncementEntityWrapper(
    announcement = testAnnouncementEntity,
    author = testAuthorEntity,
    tags = emptyList(),
    tagCrossRefs = emptyList(),
    attachments = emptyList(),
    body = testBody.copy(
        announcementId = testAnnouncementEntity.id
    )

)

val testAnnouncementWrapperEng = AnnouncementEntityWrapper(
    announcement = testAnnouncementEntityEng,
    author = testAuthorEntity,
    tags = emptyList(),
    tagCrossRefs = emptyList(),
    attachments = emptyList(),
    body = testBody.copy(
        announcementId = testAnnouncementEntityEng.id
    )
)

val testAnnouncementWrapperEvent = AnnouncementEntityWrapper(
    announcement = testAnnouncementEntityEvent,
    author = testAuthorEntity,
    tags = emptyList(),
    tagCrossRefs = emptyList(),
    attachments = emptyList(),
    body = testBody.copy(
        announcementId = testAnnouncementEntityEvent.id
    )
)

val testAnnouncementWrapperWithAttachments = AnnouncementEntityWrapper(
    announcement = testAnnouncementEntityWithAttachments,
    author = testAuthorEntity,
    tags = emptyList(),
    tagCrossRefs = emptyList(),
    attachments = testAttachmentsFor4,
    body = testBody.copy(
        announcementId = testAnnouncementEntityWithAttachments.id
    )
)

val testAnnouncementWrapperWithTags = AnnouncementEntityWrapper(
    announcement = testAnnouncementEntityWithTags,
    author = testAuthorEntity,
    tags = testTagsFor5,
    tagCrossRefs = testTagRefsFor5,
    attachments = emptyList(),
    body = testBody.copy(
        announcementId = testAnnouncementEntityWithTags.id
    )
)

val testAnnouncementWrapperCombined = AnnouncementEntityWrapper(
    announcement = testAnnouncementEntityCombined,
    author = testAuthorEntity,
    tags = testTagsFor6,
    tagCrossRefs = testTagRefsFor6,
    attachments = testAttachmentsFor6,
    body = testBody.copy(
        announcementId = testAnnouncementEntityCombined.id
    )
)

val testAnnouncementEntityWrapperList = listOf(
    testAnnouncementWrapper,
    testAnnouncementWrapperEng,
    testAnnouncementWrapperEvent,
    testAnnouncementWrapperWithAttachments,
    testAnnouncementWrapperWithTags,
    testAnnouncementWrapperCombined
)
