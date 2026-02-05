package com.kastik.apps.core.network.model.aboard.auth

import kotlinx.serialization.Serializable

@Serializable
data class AboardTokenRefreshRequestDto(
    val token: String
)