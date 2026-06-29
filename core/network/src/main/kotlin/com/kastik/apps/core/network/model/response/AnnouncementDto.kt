package com.kastik.apps.core.network.model.response

import com.kastik.apps.core.network.serializers.InstantSerializer
import com.kastik.apps.core.network.serializers.IntAsBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AnnouncementDto(
    @SerialName("id") val id: Int,
    @SerialName("title")val title: String,

    @SerialName("body") val body: String,
    @SerialName("preview")  val preview: String,

    @Serializable(with = InstantSerializer::class)
    @SerialName("created_at") val createdAt: Instant,

    @Serializable(with = InstantSerializer::class)
    @SerialName("updated_at") val updatedAt: Instant,


    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_pinned") val isPinned: Boolean,
    @SerialName("pinned_until") val pinnedUntil: String? = null,

    @SerialName("author") val author: AuthorDto,

    @SerialName("tags") val tags: List<TagResponseDto>,
    @SerialName("attachments") val attachments: List<AttachmentDto>,
)