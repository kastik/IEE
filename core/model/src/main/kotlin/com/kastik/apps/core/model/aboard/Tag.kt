package com.kastik.apps.core.model.aboard

import kotlin.time.Instant

data class Tag(
    val id: Int,
    val title: String,
    val parentId: Int? = null,
    val isPublic: Boolean = false,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val deletedAt: Instant? = null,
    val mailListName: String? = null,
    val subTags: List<Tag> = emptyList(),
)