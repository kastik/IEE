package com.kastik.apps.core.network.model.aboard.announcement

import kotlinx.serialization.Serializable

@Serializable
data class PagedAnnouncementsResponseDto(
    val data: List<AnnouncementDto>,
    val meta: PagedMetaResponseDto
)