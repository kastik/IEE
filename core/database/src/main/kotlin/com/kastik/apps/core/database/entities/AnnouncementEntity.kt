package com.kastik.apps.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AuthorEntity::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("id"),
        Index("authorId"),
    ]
)

data class AnnouncementEntity(
    @PrimaryKey val id: Int,
    val authorId: Int,
    val title: String,
    val engTitle: String?,

    val preview: String,
    val engPreview: String?,

    val hasEng: Boolean,

    val createdAt: String,
    val updatedAt: String,

    val isPinned: Boolean,
    val pinnedUntil: String?,

    val isEvent: Boolean?,
    val eventStartTime: String?,
    val eventEndTime: String?,
    val eventLocation: String?,
    val gmaps: String?,

    val announcementUrl: String
)