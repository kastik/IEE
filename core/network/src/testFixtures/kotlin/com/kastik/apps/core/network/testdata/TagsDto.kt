package com.kastik.apps.core.network.testdata

import com.kastik.apps.core.network.model.response.TagDto
import kotlin.time.Instant


val baseTagDto = TagDto(
    id = 0,
    title = "Root tag",
    parentId = null,
    isPublic = true,
    mailListName = "root_tag_mail_list",
    createdAt = Instant.fromEpochSeconds(1782774327),
    updatedAt = Instant.fromEpochSeconds(1782774327),
    deletedAt = null,
    subTags = emptyList()
)