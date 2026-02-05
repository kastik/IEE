package com.kastik.apps.core.network.model.aboard.profile

import com.kastik.apps.core.network.serializers.IntAsBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseDto(
    val id: Int,
    val name: String,
    @SerialName("name_eng") val nameEng: String,
    val email: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_author") val isAuthor: Boolean,
    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_admin") val isAdmin: Boolean,
    @SerialName("last_login_at") val lastLoginAt: String,
    val uid: String,
    @SerialName("deleted_at") val deletedAt: String?
)