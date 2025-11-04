package com.kastik.appsaboard.data.mappers

import com.kastik.appsaboard.data.datasource.remote.dto.AnnouncementDto
import com.kastik.appsaboard.domain.models.Announcement


fun Announcement.toAnnouncementDto(): AnnouncementDto = AnnouncementDto(
    id = id,
    categoryId = categoryId,
    title = title,
    titleEn = titleEn,
    text = text,
    textEn = textEn,
    publisher = publisher.toPublisherDto(),
    date = date,
    attachments = attachments
)


fun AnnouncementDto.toAnnouncement(): Announcement = Announcement(
    id = id,
    categoryId = categoryId,
    title = title,
    titleEn = titleEn,
    text = text,
    textEn = textEn,
    publisher = publisher.toPublisher(),
    date = date,
    attachments = attachments
)
