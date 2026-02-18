package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.aboard.author.AuthorResponseDto

internal val baseAuthorResponseDto = AuthorResponseDto(
    id = 0,
    name = "John",
    announcementCount = 0
)

val authorDtoTestData = listOf(
    baseAuthorResponseDto.copy(id = 1),
    baseAuthorResponseDto.copy(id = 2),
    baseAuthorResponseDto.copy(id = 3),
    baseAuthorResponseDto.copy(id = 4),
)

internal val baseAnnouncementAuthorResponseDto = AuthorResponseDto(
    id = 0,
    name = "John",
)

val announcementAuthorDtoTestData = listOf(
    baseAnnouncementAuthorResponseDto.copy(id = 1),
    baseAnnouncementAuthorResponseDto.copy(id = 2),
    baseAnnouncementAuthorResponseDto.copy(id = 3),
    baseAnnouncementAuthorResponseDto.copy(id = 4),
)