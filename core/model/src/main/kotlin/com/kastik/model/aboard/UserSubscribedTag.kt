package com.kastik.model.aboard

data class UserSubscribedTag(
    val id: Int,
    val title: String,
    val parentId: Int,
    val isPublic: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val mailListName: String,
    val pivot: Pivot,
)