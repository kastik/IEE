package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.model.aboard.AnnouncementAuthorDto
import com.kastik.apps.core.network.model.aboard.AuthorDto

fun AnnouncementAuthorDto.toAuthorEntity() = AuthorEntity(
    id = id,
    name = name,
)

fun AuthorDto.toAuthorEntity() = AuthorEntity(
    id = id,
    name = name,
    announcementCount = announcementCount
)

fun AuthorEntity.toAuthor() = Author(
    id = id,
    name = name,
    announcementCount = announcementCount ?: 0,
)