package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.common.PagedMetaDto
import com.kastik.apps.core.network.model.common.PagedResponseDto
import com.kastik.apps.core.network.model.common.SingleResponseDto
import com.kastik.apps.core.network.model.response.AnnouncementDto
import kotlin.time.Instant

val baseAnnouncementDto =
    AnnouncementDto(
        id = 1,
        title = "Test Announcement",
        body = "Test Body",
        preview = "Test Preview",
        createdAt = Instant.fromEpochSeconds(1782774327),
        updatedAt = Instant.fromEpochSeconds(1782774327),
        isPinned = false,
        pinnedUntil = null,
        tags = listOf(baseTagDto),
        attachments = listOf(baseAttachmentDto),
        author = baseAuthorDto,
    )

val baseSingleResponseAnnouncementDto =
    SingleResponseDto<AnnouncementDto>(data = baseAnnouncementDto)

internal val basePagedAnnouncementMetaDto =
    PagedMetaDto(
        currentPage = 1,
        lastPage = 1,
        path = "https://com.example.com",
        perPage = 1,
        from = 1,
        to = 1,
        total = 1,
    )

val basePagedAnnouncementDto =
    PagedResponseDto<AnnouncementDto>(
        data = listOf(baseAnnouncementDto),
        meta = basePagedAnnouncementMetaDto,
    )
