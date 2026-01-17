package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class TagsResponseDto(
    val data: List<AnnouncementTagDto>,
)