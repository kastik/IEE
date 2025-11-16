package com.kastik.apps.core.database.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kastik.apps.core.database.dao.AnnouncementDao
import org.junit.After
import org.junit.Before

internal abstract class MemoryDatabase {

    private lateinit var db: AppDatabase

    protected lateinit var announcementDao: AnnouncementDao


    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java,
            ).build()
        }
        announcementDao = db.announcementDao()
    }

    @After
    fun teardown() = db.close()

}