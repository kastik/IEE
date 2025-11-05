package com.kastik.appsaboard.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
object HomeRoute

@Serializable
data class AuthRoute(
    val code: String? = null,
    val state: String? = null,
    val error: String? = null,
    val error_description: String? = null
)

@Serializable
object ProfileRoute

@Serializable
object SettingsRoute

@Serializable
object AnnouncementRoute