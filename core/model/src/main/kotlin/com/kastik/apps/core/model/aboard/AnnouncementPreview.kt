package com.kastik.apps.core.model.aboard

data class AnnouncementPreview(
    val id: Int,
    val title: String,
    val preview: String?,
    val author: String,
    val tags: List<AnnouncementTag> = emptyList(),
    val attachments: List<AnnouncementAttachment> = emptyList(),
    val date: String
)