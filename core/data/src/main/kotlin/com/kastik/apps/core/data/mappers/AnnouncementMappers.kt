package com.kastik.apps.core.data.mappers


import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.model.AnnouncementEntityWrapper
import com.kastik.apps.core.database.model.AnnouncementWithBody
import com.kastik.apps.core.database.model.AnnouncementWithoutBody
import com.kastik.apps.core.model.aboard.AnnouncementAttachment
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementTag
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.model.aboard.AnnouncementAttachmentDto
import com.kastik.apps.core.network.model.aboard.AnnouncementDto
import com.kastik.apps.core.network.model.aboard.AnnouncementTagDto
import com.kastik.apps.core.network.model.aboard.AuthorDto


fun AnnouncementDto.toRoomEntities(): AnnouncementEntityWrapper {
    return AnnouncementEntityWrapper(
        announcement = toEntity(),
        body = toBodyEntity(),
        author = toAuthorEntity(),
        tags = toTagEntities(),
        tagCrossRefs = toTagCrossRefs(),
        attachments = toAttachmentEntities()

    )
}

private fun AnnouncementDto.toEntity() = AnnouncementEntity(
    id = id,
    title = title,
    engTitle = engTitle,
    hasEng = hasEng ?: false,
    preview = preview,
    engPreview = engPreview,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPinned = isPinned,
    pinnedUntil = pinnedUntil,
    isEvent = isEvent,
    eventStartTime = eventStartTime,
    eventEndTime = eventEndTime,
    eventLocation = eventLocation,
    gmaps = gmaps,
    announcementUrl = announcementUrl,
    authorId = author.id,
)

private fun AnnouncementDto.toAuthorEntity() = AuthorEntity(
    id = author.id,
    name = author.name
)

private fun AnnouncementDto.toAttachmentEntities() = attachments.map { att ->
    AttachmentEntity(
        id = att.id,
        announcementId = this.id,
        filename = att.filename,
        fileSize = att.filesize,
        mimeType = att.mimeType,
        attachmentUrl = att.attachmentUrl,
        attachmentUrlPreview = att.attachmentUrlView
    )
}

private fun AnnouncementDto.toTagEntities() = tags.map { tag ->
    TagEntity(
        id = tag.id,
        title = tag.title,
        parentId = tag.parentId,
        isPublic = tag.isPublic,
        mailListName = tag.mailListName,
    )
}

private fun AnnouncementDto.toTagCrossRefs() = tags.map { tag ->
    TagsCrossRefEntity(
        announcementId = this.id,
        tagId = tag.id
    )
}

private fun AnnouncementDto.toBodyEntity() = BodyEntity(
    announcementId = this.id,
    body = body,
    engBody = engBody ?: ""
)


fun AnnouncementWithoutBody.toDomain() = AnnouncementPreview(
    id = announcement.id,
    title = announcement.title,
    preview = announcement.preview,
    tags = tags.map { it.toDomain() },
    attachments = attachments.map { it.toDomain() },
    author = author.name,
    date = announcement.updatedAt,
)

internal fun AnnouncementWithBody.toDomain() = AnnouncementView(
    id = announcement.id,
    title = announcement.title,
    body = body.body,
    tags = tags.map { it.toDomain() },
    attachments = attachments.map { it.toDomain() },
    author = author.name,
    date = announcement.updatedAt,
)


internal fun AttachmentEntity.toDomain() = AnnouncementAttachment(
    id = id,
    filename = filename,
    fileSize = fileSize,
    mimeType = mimeType,
)

internal fun TagEntity.toDomain() = AnnouncementTag(
    id = id,
    title = title,
)
internal fun AnnouncementTagDto.toDomain() = AnnouncementTag(
    id = id,
    title = title,
)

internal fun AnnouncementAttachmentDto.toDomain(): AnnouncementAttachment {
    return AnnouncementAttachment(
        id = id,
        filename = filename,
        fileSize = filesize,
        mimeType = mimeType
    )

}

internal fun AuthorDto.toDomain() = Author(
    id = id,
    name = name,
    announcementCount = announcementCount
)

internal fun AnnouncementDto.toAnnouncementPreview(): AnnouncementPreview {
    return AnnouncementPreview(
        id = id,
        title = title,
        preview = preview,
        author = author.name,
        tags = tags.map { it.toDomain() },
        attachments = attachments.map { it.toDomain() },
        date = updatedAt
    )
}

internal fun AnnouncementDto.toDomain() = AnnouncementView(
    id = id,
    title = title,
    body = body,
    tags = tags.map { it.toDomain() },
    attachments = attachments.map { it.toDomain() },
    author = author.name,
    date = updatedAt,
)