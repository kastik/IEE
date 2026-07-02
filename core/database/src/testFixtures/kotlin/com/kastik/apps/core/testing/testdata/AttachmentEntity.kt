package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.AttachmentEntity

val baseAttachmentEntity = AttachmentEntity(
    id = 1,
    announcementId = 1,
    filename = "file.pdf",
    fileSize = 1024L,
    mimeType = "application/pdf",
    attachmentUrl = "https://example.com",
    attachmentUrlPreview = ""
)