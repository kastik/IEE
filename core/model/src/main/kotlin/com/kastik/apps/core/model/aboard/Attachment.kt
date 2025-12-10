package com.kastik.apps.core.model.aboard

data class Attachment(
    val id: Int,
    val filename: String,
    val fileSize: Long,
    val mimeType: String,
)