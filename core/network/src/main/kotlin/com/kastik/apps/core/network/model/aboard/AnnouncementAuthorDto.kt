package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementAuthorDto(
    val id: Int,
    val name: String
)