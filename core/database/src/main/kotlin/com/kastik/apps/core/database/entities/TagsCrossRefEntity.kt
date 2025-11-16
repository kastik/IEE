package com.kastik.apps.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index

@Entity(
    primaryKeys = ["announcementId", "tagId"],
    foreignKeys = [
        ForeignKey(
            entity = AnnouncementEntity::class,
            parentColumns = ["id"],
            childColumns = ["announcementId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tagId"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index("announcementId"),
        Index("tagId")
    ]
)
data class TagsCrossRefEntity(
    val announcementId: Int,
    val tagId: Int
)