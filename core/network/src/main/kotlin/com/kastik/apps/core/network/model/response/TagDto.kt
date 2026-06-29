package com.kastik.apps.core.network.model.response

import com.kastik.apps.core.network.serializers.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AnnouncementTagsResponseDto(
    val data: List<TagResponseDto>,
)

@Serializable
data class TagResponseDto(

    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("parent_id") val parentId: Int?,
    @SerialName("is_public") val isPublic: Boolean,
    @SerialName("maillist_name") val mailListName: String,

    @Serializable(with = InstantSerializer::class)
    @SerialName("created_at") val createdAt: Instant?,

    @Serializable(with = InstantSerializer::class)
    @SerialName("updated_at") val updatedAt: Instant?,

    @Serializable(with = InstantSerializer::class)
    @SerialName("deleted_at") val deletedAt: Instant?,

    @SerialName("childrensub_recursive") val subTags: List<TagResponseDto>?
)