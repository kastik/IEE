package com.kastik.apps.core.network.model.aboard.tags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagResponseDto(
    val id: Int,
    val title: String,
    @SerialName("parent_id") val parentId: Int? = null,
    @SerialName("is_public") val isPublic: Boolean,
    @SerialName("maillist_name") val mailListName: String? = null
)