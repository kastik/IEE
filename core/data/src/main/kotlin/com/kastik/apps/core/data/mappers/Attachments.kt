package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.network.model.aboard.AttachmentDto

internal fun AttachmentDto.toAttachment(): Attachment {
    return Attachment(
        id = id,
        filename = filename,
        fileSize = filesize,
        mimeType = mimeType
    )
}

internal fun AttachmentEntity.toAttachment() = Attachment(
    id = id,
    filename = filename,
    fileSize = fileSize,
    mimeType = mimeType,
)