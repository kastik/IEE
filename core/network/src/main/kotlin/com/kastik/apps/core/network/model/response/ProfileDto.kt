package com.kastik.apps.core.network.model.response

import com.kastik.apps.core.network.serializers.IntAsBooleanSerializer
import com.kastik.apps.core.network.serializers.StringToInstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ProfileDto(
    @SerialName("id") val id: Int,
    @SerialName("uid") val uid: String,
    @SerialName("name") val name: String,
    @SerialName("email") val email: String,
    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_admin")
    val isAdmin: Boolean,
    @Serializable(with = IntAsBooleanSerializer::class)
    @SerialName("is_author")
    val isAuthor: Boolean,
    @Serializable(with = StringToInstantSerializer::class)
    @SerialName("created_at")
    val createdAt: Instant?,
    @Serializable(with = StringToInstantSerializer::class)
    @SerialName("last_login_at")
    val lastLoginAt: Instant?,
)
