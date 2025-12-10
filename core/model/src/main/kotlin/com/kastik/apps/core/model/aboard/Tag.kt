package com.kastik.apps.core.model.aboard

data class Tag(
    val id: Int,
    val title: String
)

data class SubscribableTag(
    val id: Int,
    val title: String,
    val parentId: Int?,
    val isPublic: Boolean,
    val createdAt: String,
    val updatedAt: String?,
    val deletedAt: String?,
    val mailListName: String,
    val subTags: List<SubscribableTag>
)