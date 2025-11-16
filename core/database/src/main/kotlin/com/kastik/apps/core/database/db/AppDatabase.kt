package com.kastik.apps.core.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity

@Database(
    entities = [
        TagEntity::class,
        BodyEntity::class,
        AuthorEntity::class,
        AttachmentEntity::class,
        AnnouncementEntity::class,
        TagsCrossRefEntity::class,

    ],
    version = 1,
    exportSchema = false
)


abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
}