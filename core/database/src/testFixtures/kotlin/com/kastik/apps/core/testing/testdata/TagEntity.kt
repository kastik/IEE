package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.database.entities.TagEntity


val baseTagEntity = TagEntity(
    id = 1,
    title = "Important",
    parentId = null,
    isPublic = true,
    mailListName = "root_tag_mail_list"
)