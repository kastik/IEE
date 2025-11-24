package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementAuthorDto(
    val id: Int,
    val name: String
)

@Serializable
data class AuthorDto(
    val id: Int,
    val name: String,
    @SerialName("announcements_count") val announcementCount: Int
)