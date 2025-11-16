package com.kastik.apps.core.model.apps

data class AppsToken(
    val accessToken: String,
    val refreshToken: String?,
    val userId: String
)