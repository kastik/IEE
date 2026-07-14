package com.kastik.apps.core.model.aboard

data class Tag(
    val id: Int,
    val title: String,
    val parentId: Int? = null,
    val isPublic: Boolean = false,
    val subTags: List<Tag> = emptyList(),
)