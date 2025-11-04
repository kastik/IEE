package com.kastik.appsaboard.data.datasource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDto(
    @SerialName("_id") val id: String,
    @SerialName("_about") val categoryId: String,
    val title: String,
    val titleEn: String? = null,
    val text: String,
    val textEn: String? = null,
    val publisher: PublisherDto,
    val date: String,
    val attachments: List<String> = emptyList()
)

