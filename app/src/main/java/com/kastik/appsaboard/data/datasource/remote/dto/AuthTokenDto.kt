package com.kastik.appsaboard.data.datasource.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("user") val userId: String,
    @SerialName("refresh_token") val refreshToken: String? = null
)