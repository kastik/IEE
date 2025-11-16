package com.kastik.apps.core.testing.db


import androidx.room.InvalidationTracker
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.testing.dao.FakeAnnouncementDao

class FakeAppDatabase : AppDatabase() {
    val fakeAnnouncementDao = FakeAnnouncementDao()

    override fun announcementDao(): AnnouncementDao = fakeAnnouncementDao

    override fun createInvalidationTracker(): InvalidationTracker {
        throw Exception("Not implemented")
    }

    override fun clearAllTables() {
        throw Exception("Not implemented")
    }
}