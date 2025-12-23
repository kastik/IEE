package com.kastik.apps.core.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kastik.apps.core.database.converter.IntListConverter
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.database.dao.RemoteKeysDao
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.database.entities.AnnouncementEntity
import com.kastik.apps.core.database.entities.AttachmentEntity
import com.kastik.apps.core.database.entities.AuthorEntity
import com.kastik.apps.core.database.entities.BodyEntity
import com.kastik.apps.core.database.entities.RemoteKeys
import com.kastik.apps.core.database.entities.TagEntity
import com.kastik.apps.core.database.entities.TagsCrossRefEntity

@Database(
    entities = [
        RemoteKeys::class,
        TagEntity::class,
        BodyEntity::class,
        AuthorEntity::class,
        AttachmentEntity::class,
        AnnouncementEntity::class,
        TagsCrossRefEntity::class,
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(IntListConverter::class)


abstract class AppDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
    abstract fun authorDao(): AuthorsDao
    abstract fun tagsDao(): TagsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}