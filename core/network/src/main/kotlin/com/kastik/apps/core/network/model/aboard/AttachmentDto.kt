package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachmentDto(
    val id: Int,
    @SerialName("announcement_id") val announcementId: Int,
    val filename: String,
    val filesize: Long,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("attachment_url") val attachmentUrl: String,
    @SerialName("attachment_url_view") val attachmentUrlView: String
)