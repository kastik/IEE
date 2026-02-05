package com.kastik.apps.core.network.model.aboard.announcement

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementResponseDto(
    val data: AnnouncementDto,
)