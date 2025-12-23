package com.kastik.apps.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.database.dao.RemoteKeysDao
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.database.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, "announcement_cache.db"
    ).fallbackToDestructiveMigration(true).build()

    @Provides
    fun provideAnnouncementDao(appDatabase: AppDatabase): AnnouncementDao {
        return appDatabase.announcementDao()
    }

    @Provides
    fun provideAuthorDao(appDatabase: AppDatabase): AuthorsDao {
        return appDatabase.authorDao()
    }

    @Provides
    fun provideTagsDao(appDatabase: AppDatabase): TagsDao {
        return appDatabase.tagsDao()
    }

    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.remoteKeysDao()
    }

    @Provides
    @DbTransaction
    fun provideTransactionLambda(db: AppDatabase): suspend (suspend () -> Unit) -> Unit {
        return { block -> db.withTransaction(block) }
    }

}