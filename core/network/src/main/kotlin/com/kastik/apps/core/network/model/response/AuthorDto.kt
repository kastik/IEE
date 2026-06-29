package com.kastik.apps.core.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(

    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("announcements_count") val announcementCount: Int? = null

)