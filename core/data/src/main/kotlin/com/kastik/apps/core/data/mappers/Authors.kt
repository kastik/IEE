package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.model.response.AuthorDto

fun AuthorDto.toAuthorEntity() = AuthorEntity(
    id = id,
    name = name,
    announcementCount = announcementCount
)

fun AuthorEntity.toAuthor() = Author(
    id = id,
    name = name,
    announcementCount = announcementCount,
)