package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.data.mappers.toAttachmentEntity
import com.kastik.apps.core.network.model.aboard.AttachmentDto

private val baseAttachmentDto = AttachmentDto(
    id = 0,
    announcementId = 1,
    filename = "dilosi.pdf",
    filesize = 2000,
    mimeType = "application/pdf",
    attachmentUrl = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
    attachmentUrlView = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
)

val attachmentDtoTestData = listOf(
    baseAttachmentDto.copy(id = 1),
    baseAttachmentDto.copy(id = 2),
    baseAttachmentDto.copy(id = 3),
    baseAttachmentDto.copy(id = 4),
    baseAttachmentDto.copy(id = 5),
)

val attachmentEntityTestData = attachmentDtoTestData.map { it.toAttachmentEntity() }