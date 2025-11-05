package com.kastik.appsaboard.domain.models


data class AuthToken(
    val accessToken: String,
    val refreshToken: String?,
    val userId: String
)