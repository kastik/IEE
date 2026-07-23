package com.kastik.apps.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(
    foreignKeys =
        [
            ForeignKey(
                entity = AuthorEntity::class,
                parentColumns = ["id"],
                childColumns = ["authorId"],
                onDelete = CASCADE,
            )
        ],
    indices =
        [
            Index("id"),
            Index("authorId"),
        ],
)
data class AnnouncementEntity(
    @PrimaryKey val id: Int,
    val authorId: Int,
    val title: String,
    val preview: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isPinned: Boolean,
    val pinnedUntil: String?,
)
