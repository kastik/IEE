package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.network.model.response.AttachmentDto


fun AttachmentDto.toAttachmentEntity() = AttachmentEntity(
    id = id,
    announcementId = announcementId,
    filename = fileName,
    fileSize = fileSize,
    mimeType = mimeType,
    attachmentUrl = attachmentUrl,
    attachmentUrlPreview = attachmentUrlView
)

fun AttachmentEntity.toAttachment() = Attachment(
    id = id,
    fileName = filename,
    fileSize = fileSize,
    mimeType = mimeType,
)