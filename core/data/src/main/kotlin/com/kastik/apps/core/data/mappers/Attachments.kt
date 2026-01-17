package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.network.model.aboard.AttachmentDto


fun AttachmentDto.toAttachmentEntity() = AttachmentEntity(
    id = id,
    announcementId = announcementId,
    filename = filename,
    fileSize = filesize,
    mimeType = mimeType,
    attachmentUrl = attachmentUrl,
    attachmentUrlPreview = attachmentUrlView
)

fun AttachmentEntity.toAttachment() = Attachment(
    id = id,
    filename = filename,
    fileSize = fileSize,
    mimeType = mimeType,
)