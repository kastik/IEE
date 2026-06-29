package com.kastik.apps.core.model.aboard

import kotlin.time.Instant


data class Profile(
    val id: Int,
    val uid: String,
    val name: String,
    val email: String,
    val isAdmin: Boolean = false,
    val isAuthor: Boolean = false,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val deletedAt: Instant? = null,
    val lastLoginAt: Instant? = null,
)