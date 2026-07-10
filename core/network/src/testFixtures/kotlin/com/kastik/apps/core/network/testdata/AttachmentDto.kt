package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.response.AttachmentDto


val baseAttachmentDto = AttachmentDto(
    id = 0,
    announcementId = 1,
    fileName = "dilosi.pdf",
    fileSize = 2000,
    mimeType = "application/pdf",
    attachmentUrl = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
    attachmentUrlView = "https://aboard.iee.ihu.gr/api/v2/announcements/1/attachments/1",
)