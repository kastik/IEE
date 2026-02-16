package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.AuthorEntity


internal val baseAuthorEntity = AuthorEntity(
    id = 0,
    name = "John",
    announcementCount = 0
)

val announcementAuthorEntityTestData = listOf(
    baseAuthorEntity.copy(id = 1),
    baseAuthorEntity.copy(id = 2),
    baseAuthorEntity.copy(id = 3),
    baseAuthorEntity.copy(id = 4),
    AuthorEntity(id = 999, name = "Kostas")
)