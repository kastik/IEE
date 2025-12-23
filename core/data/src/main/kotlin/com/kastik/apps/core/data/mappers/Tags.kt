package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.network.model.aboard.AnnouncementTagDto


internal fun AnnouncementTagDto.toTag() = Tag(
    id = id,
    title = title,
)

fun AnnouncementTagDto.toTagEntity() = TagEntity(
    id = id,
    title = title,
    parentId = parentId,
    isPublic = isPublic,
    mailListName = mailListName
)

internal fun TagEntity.toTag() = Tag(
    id = id,
    title = title,
)