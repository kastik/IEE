package com.kastik.apps.core.model.aboard

data class AnnouncementView(
    val id: Int,
    val title: String,
    val body: String,
    val author: String,
    val tags: List<AnnouncementTag> = emptyList(),
    val attachments: List<AnnouncementAttachment> = emptyList(),
    val date: String,
)