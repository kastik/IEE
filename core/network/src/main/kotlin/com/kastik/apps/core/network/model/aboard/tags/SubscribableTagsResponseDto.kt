package com.kastik.apps.core.network.model.aboard.tags

import com.kastik.apps.core.network.serializers.InstantSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class SubscribableTagsResponseDto(
    val id: Int,
    val title: String,
    @SerialName("parent_id") val parentId: Int?,
    @SerialName("is_public") val isPublic: Boolean,
    @Serializable(with = InstantSerializer::class)
    @SerialName("created_at") val createdAt: Instant?,
    @Serializable(with = InstantSerializer::class)
    @SerialName("updated_at") val updatedAt: Instant?,
    @Serializable(with = InstantSerializer::class)
    @SerialName("deleted_at") val deletedAt: Instant?,
    @SerialName("maillist_name") val mailListName: String,
    @SerialName("childrensub_recursive") val subTags: List<SubscribableTagsResponseDto>
)