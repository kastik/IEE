package com.kastik.apps.core.network.model.aboard.profile

import com.kastik.apps.core.network.serializers.InstantSerializer
import com.kastik.apps.core.network.serializers.IntAsBooleanSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ProfileResponseDto(
    val id: Int,
    val name: String,
    @SerialName("name_eng") val nameEng: String,
    val email: String,
    @Serializable(with = InstantSerializer::class)
    @SerialName("created_at") val createdAt: Instant?,
    @Serializable(with = InstantSerializer::class)
    @SerialName("updated_at") val updatedAt: Instant?,
    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_author") val isAuthor: Boolean,
    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_admin") val isAdmin: Boolean,
    @Serializable(with = InstantSerializer::class)
    @SerialName("last_login_at") val lastLoginAt: Instant?,
    val uid: String,
    @Serializable(with = InstantSerializer::class)
    @SerialName("deleted_at") val deletedAt: Instant?
)