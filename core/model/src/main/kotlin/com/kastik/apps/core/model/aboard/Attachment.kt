package com.kastik.apps.core.model.aboard

data class Attachment(
    val id: Int,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
)