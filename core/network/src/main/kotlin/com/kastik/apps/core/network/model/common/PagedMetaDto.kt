package com.kastik.apps.core.network.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagedMetaDto(
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("path") val path: String,
    @SerialName("per_page") val perPage: Int,
    @SerialName("from") val from: Int,
    @SerialName("to") val to: Int,
    @SerialName("total") val total: Int,
)
