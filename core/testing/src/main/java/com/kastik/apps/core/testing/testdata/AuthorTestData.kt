package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.data.mappers.toAuthorEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.network.model.aboard.AnnouncementAuthorDto
import com.kastik.apps.core.network.model.aboard.AuthorDto

private val baseAuthorDto = AuthorDto(
    id = 0,
    name = "John",
    announcementCount = 0
)

val authorDtoTestData = listOf(
    baseAuthorDto.copy(id = 1),
    baseAuthorDto.copy(id = 2),
    baseAuthorDto.copy(id = 3),
    baseAuthorDto.copy(id = 4),
)

private val baseAnnouncementAuthorDto = AnnouncementAuthorDto(
    id = 0,
    name = "John",
)

val announcementAuthorDtoTestData = listOf(
    baseAnnouncementAuthorDto.copy(id = 1),
    baseAnnouncementAuthorDto.copy(id = 2),
    baseAnnouncementAuthorDto.copy(id = 3),
    baseAnnouncementAuthorDto.copy(id = 4),
)

val announcementAuthorEntityTestData =
    authorDtoTestData.map { it.toAuthorEntity() } + AuthorEntity(id = 999, name = "Kostas")