package com.kastik.apps.core.network.model.aboard.tags

import kotlinx.serialization.Serializable

@Serializable
data class TagsResponseDto(
    val data: List<TagResponseDto>,
)