package com.kastik.appsaboard.domain.models

data class Announcement(
    val id: String,
    val categoryId: String,
    val title: String,
    val titleEn: String? = null,
    val text: String,
    val textEn: String? = null,
    val publisher: Publisher,
    val date: String,
    val attachments: List<String> = emptyList()
)