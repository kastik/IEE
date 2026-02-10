package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.data.utils.Base64ImageExtractor
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity
import com.kastik.apps.core.database.relations.AnnouncementDetailRelation
import com.kastik.apps.core.database.relations.AnnouncementPreviewRelation
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.network.model.aboard.announcement.AnnouncementDto

fun AnnouncementDto.toAnnouncement() = Announcement(
    id = id,
    title = title,
    preview = preview,
    body = body,
    author = author.name,
    date = createdAt,
    pinned = isPinned,
)

fun AnnouncementDto.toAnnouncementEntity() = AnnouncementEntity(
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

fun AnnouncementDto.toTagCrossRefs() = tags.map { tag ->
    TagsCrossRefEntity(
        announcementId = this.id,
        tagId = tag.id
    )
}
suspend fun AnnouncementDto.extractImages(extractor: Base64ImageExtractor) =
    this.copy(
        body = extractor.process(this.body)
    )

fun AnnouncementDto.toBodyEntity() = BodyEntity(
    announcementId = this.id,
    body = body,
    engBody = engBody ?: ""
)

fun AnnouncementPreviewRelation.toAnnouncement() = Announcement(
    id = announcement.id,
    title = announcement.title,
    preview = announcement.preview,
    tags = tags.map { it.toTag() },
    attachments = attachments.map { it.toAttachment() },
    author = author.name,
    date = announcement.updatedAt,
    pinned = announcement.isPinned,
    body = ""
)

fun AnnouncementDetailRelation.toAnnouncement() = Announcement(
    id = announcement.id,
    title = announcement.title,
    body = body.body,
    tags = tags.map { it.toTag() },
    attachments = attachments.map { it.toAttachment() },
    author = author.name,
    date = announcement.updatedAt,
    pinned = announcement.isPinned,
    preview = announcement.preview
)