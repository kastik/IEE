package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.aboard.attachment.AttachmentResponseDto


internal val baseAttachmentResponseDto = AttachmentResponseDto(
    id = 0,
    announcementId = 1,
    filename = "dilosi.pdf",
    filesize = 2000,
    mimeType = "application/pdf",
    attachmentUrl = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
    attachmentUrlView = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
)

val attachmentDtoTestData = listOf(
    baseAttachmentResponseDto.copy(id = 1),
    baseAttachmentResponseDto.copy(id = 2),
    baseAttachmentResponseDto.copy(id = 3),
    baseAttachmentResponseDto.copy(id = 4),
    baseAttachmentResponseDto.copy(id = 5),
)

