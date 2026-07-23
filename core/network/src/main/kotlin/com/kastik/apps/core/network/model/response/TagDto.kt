package com.kastik.apps.core.network.model.response

import com.kastik.apps.core.network.serializers.StringToInstantSerializer
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("parent_id") val parentId: Int?,
    @SerialName("is_public") val isPublic: Boolean,
    @SerialName("maillist_name") val mailListName: String,
    @Serializable(with = StringToInstantSerializer::class)
    @SerialName("created_at")
    val createdAt: Instant?,
    @Serializable(with = StringToInstantSerializer::class)
    @SerialName("updated_at")
    val updatedAt: Instant?,
    @Serializable(with = StringToInstantSerializer::class)
    @SerialName("deleted_at")
    val deletedAt: Instant?,
    @SerialName("childrensub_recursive") val subTags: List<TagDto>?,
)
