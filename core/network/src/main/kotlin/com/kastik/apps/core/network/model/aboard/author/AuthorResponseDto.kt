package com.kastik.apps.core.network.model.aboard.author

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorResponseDto(
    val id: Int,
    val name: String,
    @SerialName("announcements_count") val announcementCount: Int? = null
)