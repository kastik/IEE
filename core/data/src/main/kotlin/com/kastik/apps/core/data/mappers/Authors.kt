package com.kastik.apps.core.data.mappers

import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.network.model.aboard.AuthorDto

internal fun AuthorDto.toAuthor() = Author(
    id = id,
    name = name,
    announcementCount = announcementCount
)