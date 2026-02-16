package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedAnnouncementsResponseDto
import com.kastik.apps.core.network.model.aboard.announcement.PagedMetaResponseDto

internal val baseAnnouncementDto = AnnouncementDto(
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
    author = announcementAuthorDtoTestData.first(),
)

val announcementDtoTestData = listOf(
    baseAnnouncementDto,
    baseAnnouncementDto.copy(
        id = 2,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 3,
        isEvent = true,
        eventStartTime = "2025-10-25 11:44",
        eventEndTime = "2025-10-26 11:44",
        eventLocation = "IHU-Campus",
        gmaps = "https://www.google.com/maps/something"
    ),
    baseAnnouncementDto.copy(
        id = 4,
        attachments = attachmentDtoTestData.map { it.copy(announcementId = 4) }
    ),
    baseAnnouncementDto.copy(
        id = 5,
        tags = announcementTagDtoTestData
    ),
    baseAnnouncementDto.copy(
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
        tags = announcementTagDtoTestData,
        attachments = attachmentDtoTestData.map { it.copy(announcementId = 6) }
    ),
    baseAnnouncementDto.copy(
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
        tags = announcementTagDtoTestData,
        attachments = attachmentDtoTestData.map { it.copy(announcementId = 7) }
    ),
    baseAnnouncementDto.copy(
        id = 8,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 9,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 10,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 11,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 12,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 13,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 14,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 15,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 16,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 17,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 18,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 19,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
    baseAnnouncementDto.copy(
        id = 20,
        engTitle = "Test Announcement Eng",
        engBody = "Test Body Eng",
        engPreview = "Test Preview Eng",
        hasEng = true
    ),
)


internal val basePagedAnnouncementsResponseDtoTestData = PagedAnnouncementsResponseDto(
    data = announcementDtoTestData,
    meta = PagedMetaResponseDto(
        currentPage = 1,
        from = 1,
        lastPage = 100,
        path = "https://aboard.iee.ihu.gr/api/v2/announcements",
        perPage = 10,
        to = 10,
        total = 10
    )
)

val announcementPageResponseTestData = listOf(
    basePagedAnnouncementsResponseDtoTestData,
    basePagedAnnouncementsResponseDtoTestData.copy(
        data = basePagedAnnouncementsResponseDtoTestData.data.map { it.copy(id = it.id + 20) },
        meta = basePagedAnnouncementsResponseDtoTestData.meta.copy(
            currentPage = 2
        )
    ),
    basePagedAnnouncementsResponseDtoTestData.copy(
        data = basePagedAnnouncementsResponseDtoTestData.data.map { it.copy(id = it.id + 40) },
        meta = basePagedAnnouncementsResponseDtoTestData.meta.copy(
            currentPage = 3
        )
    ),
    basePagedAnnouncementsResponseDtoTestData.copy(
        data = basePagedAnnouncementsResponseDtoTestData.data.map { it.copy(id = it.id + 60) },
        meta = basePagedAnnouncementsResponseDtoTestData.meta.copy(
            currentPage = 4
        )
    ),
)