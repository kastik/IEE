package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class TagsResponse(
    val data: List<AnnouncementTagDto>,
)