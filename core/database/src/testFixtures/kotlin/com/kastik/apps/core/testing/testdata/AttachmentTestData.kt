package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.AttachmentEntity

internal val baseAttachmentEntity = AttachmentEntity(
    id = 1,
    announcementId = 1,
    filename = "dilosi.pdf",
    fileSize = 2000,
    mimeType = "application/pdf",
    attachmentUrl = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
    attachmentUrlPreview = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
)

val attachmentEntityTestData = listOf(
    baseAttachmentEntity,
    baseAttachmentEntity.copy(
        id = 2,
        announcementId = 2,
    ),
    baseAttachmentEntity.copy(
        id = 3,
        announcementId = 2
    ),
    baseAttachmentEntity.copy(id = 4),
    baseAttachmentEntity.copy(id = 5),
)