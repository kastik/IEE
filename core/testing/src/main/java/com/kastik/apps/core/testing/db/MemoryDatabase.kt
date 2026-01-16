package com.kastik.apps.core.testing.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.database.dao.RemoteKeysDao
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.database.db.AppDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.asExecutor
import org.junit.After
import java.util.concurrent.Executors

abstract class MemoryDatabase(
    protected val testDispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor()
        .asCoroutineDispatcher()
) {

    protected val db: AppDatabase = run {
        val context = ApplicationProvider.getApplicationContext<Context>()
        Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
        )
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .allowMainThreadQueries()

            .build()
    }

    protected val announcementDao: AnnouncementDao = db.announcementDao()
    protected val authorsDao: AuthorsDao = db.authorDao()
    protected val tagsDao: TagsDao = db.tagsDao()
    protected val remoteKeysDao: RemoteKeysDao = db.remoteKeysDao()

    @After
    fun closeDb() {
        println("Closing database")
        db.close()
    }

}