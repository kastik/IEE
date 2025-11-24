package com.kastik.apps.navigation

import kotlinx.serialization.Serializable


@Serializable
object HomeRoute

@Serializable
object AnnouncementListRoute

@Serializable
object AnnouncementSearchRoute

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
data class AnnouncementRoute(
    val id: Int
)

@Serializable
object NotFoundRoute