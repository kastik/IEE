package com.kastik.apps.core.model.aboard

data class AnnouncementAttachment(
    val id: Int,
    val filename: String,
    val fileSize: Long,
    val mimeType: String,
)