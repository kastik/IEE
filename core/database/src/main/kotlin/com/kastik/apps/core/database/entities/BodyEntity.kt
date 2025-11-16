package com.kastik.apps.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BodyEntity(
    @PrimaryKey val announcementId: Int,
    val body: String,
    val engBody: String,
)