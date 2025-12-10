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
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import com.kastik.apps.core.network.model.aboard.AnnouncementDto

internal fun AnnouncementDto.toAnnouncementPreview(): AnnouncementPreview {
    return AnnouncementPreview(
        id = id,
        title = title,
        preview = preview,
        author = author.name,
        tags = tags.map { it.toTag() },
        attachments = attachments.map { it.toAttachment() },
        date = updatedAt
    )
}

internal fun AnnouncementDto.toAnnouncementView() = AnnouncementView(
    id = id,
    title = title,
    body = body,
    tags = tags.map { it.toTag() },
    attachments = attachments.map { it.toAttachment() },
    author = author.name,
    date = updatedAt,
)

internal fun AnnouncementWithoutBody.toAnnouncementPreview() = AnnouncementPreview(
    id = announcement.id,
    title = announcement.title,
    preview = announcement.preview,
    tags = tags.map { it.toTag() },
    attachments = attachments.map { it.toAttachment() },
    author = author.name,
    date = announcement.updatedAt,
)

internal fun AnnouncementWithBody.toAnnouncementView() = AnnouncementView(
    id = announcement.id,
    title = announcement.title,
    body = body.body,
    tags = tags.map { it.toTag() },
    attachments = attachments.map { it.toAttachment() },
    author = author.name,
    date = announcement.updatedAt,
)

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