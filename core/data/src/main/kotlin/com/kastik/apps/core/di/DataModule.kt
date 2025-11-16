package com.kastik.apps.core.di

import android.content.Context
import androidx.room.Room
import com.kastik.apps.core.data.repository.AnnouncementRepoImpl
import com.kastik.apps.core.data.repository.AuthenticationRepositoryImpl
import com.kastik.apps.core.data.repository.UserPreferencesRepoImpl
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.UserPreferencesLocalDataSource
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSourceImpl
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, "apps_database.db"
    ).fallbackToDestructiveMigration(true)
        .build()

    @Provides
    fun provideAnnouncementDao(db: AppDatabase) = db.announcementDao()


    @Provides
    @Singleton
    fun provideAnnouncementRemoteDataSource(
        api: AboardApiClient
    ): AnnouncementRemoteDataSource = AnnouncementRemoteDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideAnnouncementRepository(
        remoteDataSource: AnnouncementRemoteDataSource,
        database: AppDatabase,
    ): AnnouncementRepository = AnnouncementRepoImpl(remoteDataSource, database)


    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        local: AuthenticationLocalDataSource, remote: AuthenticationRemoteDataSource
    ): AuthenticationRepository = AuthenticationRepositoryImpl(local, remote)

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        datastore: UserPreferencesLocalDataSource
    ): UserPreferencesRepository = UserPreferencesRepoImpl(datastore)

}

