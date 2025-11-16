package com.kastik.apps.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val parentId: Int?,
    val isPublic: Boolean,
    val mailListName: String?
)