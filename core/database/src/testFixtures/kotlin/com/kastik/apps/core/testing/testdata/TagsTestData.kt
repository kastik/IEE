package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.TagEntity


internal val baseTagEntity = TagEntity(
    id = 1,
    title = "Root Tag",
    parentId = null,
    isPublic = true,
    mailListName = "root_tag_mail_list"
)

val announcementTagEntityTestData = listOf(
    baseTagEntity,
    baseTagEntity.copy(id = 2, parentId = 1, title = "Sub Tag 1"),
    baseTagEntity.copy(id = 3, parentId = 1, title = "Sub Tag 2"),
    baseTagEntity.copy(id = 4, parentId = 2, title = "Sub Tag 3"),
    baseTagEntity.copy(id = 5, parentId = 2, title = "Sub Tag 4"),
)