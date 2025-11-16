package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.AnnouncementAttachmentDto
import com.kastik.apps.core.network.model.aboard.AnnouncementAuthorDto
import com.kastik.apps.core.network.model.aboard.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.AnnouncementTagDto

val testAnnouncementAuthorDto = AnnouncementAuthorDto(
    id = 1, name = "Kostas"
)

val testAnnouncementAttachmentDto = AnnouncementAttachmentDto(
    id = 0,
    announcementId = 4,
    filename = "dilosi.pdf",
    filesize = 2000,
    mimeType = "application/pdf",
    attachmentUrl = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
    attachmentUrlView = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1"
)

val testAnnouncementTags = AnnouncementTagDto(
    id = 1, title = "tag", parentId = null, isPublic = false, mailListName = "no-mail"
)

val testAnnouncementDto = AnnouncementDto(
    id = 1,
    title = "Test Announcement",
    engTitle = null,
    body = "Test Body",
    engBody = null,
    preview = "Test Preview",
    engPreview = null,
    hasEng = null,
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
    tags = emptyList(),
    attachments = emptyList(),
    author = testAnnouncementAuthorDto,
)
val testAnnouncementDtoEng = testAnnouncementDto.copy(
    id = 2,
    engTitle = "Test Announcement Eng",
    engBody = "Test Body Eng",
    engPreview = "Test Preview Eng",
    hasEng = true,
)

val testAnnouncementDtoEvent = testAnnouncementDto.copy(
    id = 3,
    isEvent = true,
    eventStartTime = "2025-10-25 11:44",
    eventEndTime = "2025-10-26 11:44",
    eventLocation = "IHU-Campus",
    gmaps = "https://www.google.com/maps/something"
)

val testAnnouncementDtoWithAttachment = testAnnouncementDto.copy(
    id = 4, attachments = listOf(
        testAnnouncementAttachmentDto.copy(
            id = 1,
            announcementId = 4,
        ), testAnnouncementAttachmentDto.copy(
            id = 2,
            announcementId = 4,
        ), testAnnouncementAttachmentDto.copy(
            id = 3,
            announcementId = 4,
        )
    )
)

val testAnnouncementDtoWithTags = testAnnouncementDto.copy(
    id = 5, tags = listOf(
        testAnnouncementTags, testAnnouncementTags.copy(
            id = 2, title = "tag2"
        ), testAnnouncementTags.copy(
            id = 3, title = "tag3"
        )
    )
)
val testAnnouncementDtoCombined = testAnnouncementDto.copy(
    id = 6,
    engTitle = "Test Announcement Eng",
    engBody = "Test Body Eng",
    engPreview = "Test Preview Eng",
    hasEng = true,
    isEvent = true,
    eventStartTime = "2025-10-25 11:44",
    eventEndTime = "2025-10-26 11:44",
    eventLocation = "IHU-Campus",
    gmaps = "https://www.google.com/maps/something",
    tags = listOf(
        testAnnouncementTags, testAnnouncementTags.copy(
            id = 2, title = "tag2"
        ), testAnnouncementTags.copy(
            id = 3, title = "tag3"
        )
    ),
    attachments = listOf(
        testAnnouncementAttachmentDto.copy(
            id = 4,
            announcementId = 6,
        ), testAnnouncementAttachmentDto.copy(
            id = 5,
            announcementId = 6,
        ), testAnnouncementAttachmentDto.copy(
            id = 6,
            announcementId = 6,
        )
    ),
)

val testAnnouncementDtoCombined2 = testAnnouncementDto.copy(
    id = 7,
    engTitle = "Test Announcement Eng",
    engBody = "Test Body Eng",
    engPreview = "Test Preview Eng",
    hasEng = true,
    isEvent = true,
    eventStartTime = "2025-10-25 11:44",
    eventEndTime = "2025-10-26 11:44",
    eventLocation = "IHU-Campus",
    gmaps = "https://www.google.com/maps/something",
    tags = listOf(
        testAnnouncementTags, testAnnouncementTags.copy(
            id = 2, title = "tag2"
        ), testAnnouncementTags.copy(
            id = 3, title = "tag3"
        )
    ),
    attachments = listOf(
        testAnnouncementAttachmentDto.copy(
            id = 7,
            announcementId = 7,
        ), testAnnouncementAttachmentDto.copy(
            id = 8,
            announcementId = 7,
        ), testAnnouncementAttachmentDto.copy(
            id = 9,
            announcementId = 7,
        )
    ),
)

val testAnnouncementDtoEng2 = testAnnouncementDto.copy(
    id = 8,
    engTitle = "Test Announcement Eng",
    engBody = "Test Body Eng",
    engPreview = "Test Preview Eng",
    hasEng = true,
)

val testAnnouncementDtoEng3 = testAnnouncementDto.copy(
    id = 9,
    engTitle = "Test Announcement Eng",
    engBody = "Test Body Eng",
    engPreview = "Test Preview Eng",
    hasEng = true,
)

val testAnnouncementDtoEng4 = testAnnouncementDto.copy(
    id = 10,
    engTitle = "Test Announcement Eng",
    engBody = "Test Body Eng",
    engPreview = "Test Preview Eng",
    hasEng = true,
)


val testAnnouncementDtoList = listOf(
    testAnnouncementDto,
    testAnnouncementDtoEng,
    testAnnouncementDtoEvent,
    testAnnouncementDtoWithAttachment,
    testAnnouncementDtoWithTags,
    testAnnouncementDtoCombined,
    testAnnouncementDtoCombined2,
    testAnnouncementDtoEng2,
    testAnnouncementDtoEng3,
    testAnnouncementDtoEng4,
)