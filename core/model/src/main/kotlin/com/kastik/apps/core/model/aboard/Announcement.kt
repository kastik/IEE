package com.kastik.apps.core.model.aboard

data class AnnouncementView(
    val id: Int,
    val title: String,
    val body: String,
    val author: String,
    val tags: List<Tag> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val date: String,
)

data class AnnouncementPreview(
    val id: Int,
    val title: String,
    val preview: String?,
    val author: String,
    val tags: List<Tag> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val date: String
)