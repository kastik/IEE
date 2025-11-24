package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserSubscribedTagDto(
    val id: Int,
    val title: String,
    @SerialName("parent_id") val parentId: Int?,
    @SerialName("is_public") val isPublic: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("deleted_at") val deletedAt: String?,
    @SerialName("maillist_name") val mailListName: String,
    val pivot: PivotDto,
)