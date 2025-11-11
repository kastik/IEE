package com.kastik.model.aboard

data class UserProfile(
    val id: Int,
    val name: String,
    val nameEng: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String,
    val isAuthor: Boolean,
    val isAdmin: Boolean,
    val lastLoginAt: String,
    val uid: String,
    val deletedAt: String?
)