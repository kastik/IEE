package com.kastik.network.model.aboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PivotDto(
    @SerialName("user_id") val userId: Int,
    @SerialName("tag_id") val tagId: Int
)