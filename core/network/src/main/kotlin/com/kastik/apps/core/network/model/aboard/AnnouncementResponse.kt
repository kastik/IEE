package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.Serializable

@Serializable
data class SingleAnnouncementResponse(
    val data: AnnouncementDto,
)