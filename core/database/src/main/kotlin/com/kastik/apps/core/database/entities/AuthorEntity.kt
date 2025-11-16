package com.kastik.apps.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class AuthorEntity(
    @PrimaryKey val id: Int,
    val name: String,
)