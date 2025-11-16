package com.kastik.apps.core.network.model.aboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementMeta(
    @SerialName("current_page") val currentPage: Int,
    val from: Int,
    @SerialName("last_page") val lastPage: Int,
    val path: String,
    @SerialName("per_page") val perPage: Int,
    val to: Int,
    val total: Int
)