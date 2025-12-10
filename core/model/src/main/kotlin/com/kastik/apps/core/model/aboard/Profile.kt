package com.kastik.apps.core.model.aboard

data class Profile(
    val id: Int,
    val name: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val isAuthor: Boolean,
    val isAdmin: Boolean,
    val lastLoginAt: String,
    val uid: String,
    val deletedAt: String?
)