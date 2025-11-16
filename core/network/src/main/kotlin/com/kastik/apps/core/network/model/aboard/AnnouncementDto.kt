package com.kastik.apps.core.network.model.aboard

import com.kastik.apps.core.network.serializers.IntAsBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDto(
    val id: Int,
    val title: String,
    @SerialName("eng_title") val engTitle: String? = null,
    val body: String,
    @SerialName("eng_body") val engBody: String? = null,
    val preview: String,
    @SerialName("eng_preview") val engPreview: String? = null,
    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("has_eng") val hasEng: Boolean? = null,


    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,


    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_pinned") val isPinned: Boolean,
    @SerialName("pinned_until") val pinnedUntil: String? = null,

    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_event") val isEvent: Boolean? = null,
    @SerialName("event_start_time") val eventStartTime: String? = null,
    @SerialName("event_end_time") val eventEndTime: String? = null,
    @SerialName("event_location") val eventLocation: String? = null,
    val gmaps: String? = null,


    val tags: List<AnnouncementTagDto> = emptyList(),
    val attachments: List<AnnouncementAttachmentDto> = emptyList(),
    val author: AnnouncementAuthorDto,


    @SerialName("announcement_url") val announcementUrl: String
)