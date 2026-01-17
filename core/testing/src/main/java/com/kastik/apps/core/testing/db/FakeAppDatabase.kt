package com.kastik.apps.core.testing.db


import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.database.dao.RemoteKeysDao
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.testing.dao.FakeAnnouncementDao
import com.kastik.apps.core.testing.dao.FakeAuthorsDao
import com.kastik.apps.core.testing.dao.FakeRemoteKeysDao
import com.kastik.apps.core.testing.dao.FakeTagsDao

class FakeAppDatabase : AppDatabase() {
    val fakeAnnouncementDao = FakeAnnouncementDao()
    val fakeRemoteKeysDao = FakeRemoteKeysDao()
    val fakeAuthorDao = FakeAuthorsDao()
    val fakeTagsDao = FakeTagsDao()


    override fun announcementDao(): AnnouncementDao = fakeAnnouncementDao
    override fun authorDao(): AuthorsDao = fakeAuthorDao
    override fun tagsDao(): TagsDao = fakeTagsDao
    override fun remoteKeysDao(): RemoteKeysDao = fakeRemoteKeysDao


    override fun createInvalidationTracker(): InvalidationTracker {
        throw Exception("Not implemented")
    }

    override fun clearAllTables() {
        throw Exception("Not implemented")
    }
}