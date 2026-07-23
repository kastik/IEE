package com.kastik.apps.core.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachmentDto(
    @SerialName("id") val id: Int,
    @SerialName("announcement_id") val announcementId: Int,
    @SerialName("filename") val fileName: String,
    @SerialName("filesize") val fileSize: Long,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("attachment_url") val attachmentUrl: String,
    @SerialName("attachment_url_view") val attachmentUrlView: String,
)
