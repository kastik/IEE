package com.kastik.apps.core.network.model.apps

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppsAuthTokenDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("user") val userId: String,
    @SerialName("refresh_token") val refreshToken: String? = null
)
