package com.kastik.apps.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AnnouncementEntity::class,
            parentColumns = ["id"],
            childColumns = ["announcementId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("id"),
        Index("announcementId"),
    ]
)
data class AttachmentEntity(
    @PrimaryKey val id: Int,
    val announcementId: Int,
    val filename: String,
    val fileSize: Long,
    val mimeType: String,
    val attachmentUrl: String,
    val attachmentUrlPreview: String,
)