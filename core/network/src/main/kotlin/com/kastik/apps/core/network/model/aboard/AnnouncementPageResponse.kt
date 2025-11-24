package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementPageResponse(
    val data: List<AnnouncementDto>,
    val meta: AnnouncementMeta
)