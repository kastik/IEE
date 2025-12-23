package com.kastik.apps.core.di

import com.kastik.apps.core.data.repository.AnnouncementRepositoryImpl
import com.kastik.apps.core.data.repository.AuthenticationRepositoryImpl
import com.kastik.apps.core.data.repository.AuthorRepositoryImpl
import com.kastik.apps.core.data.repository.ProfileRepositoryImpl
import com.kastik.apps.core.data.repository.TagsRepositoryImpl
import com.kastik.apps.core.data.repository.UserPreferencesRepoImpl
import com.kastik.apps.core.database.dao.AnnouncementDao
import com.kastik.apps.core.database.dao.AuthorsDao
import com.kastik.apps.core.database.dao.TagsDao
import com.kastik.apps.core.database.db.AppDatabase
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.datastore.TagsLocalDataSource
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import com.kastik.apps.core.notifications.PushNotificationsDatasource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAnnouncementRepository(
        database: AppDatabase,
        announcementLocalDataSource: AnnouncementDao,
        announcementRemoteDataSource: AnnouncementRemoteDataSource,
    ): AnnouncementRepository = AnnouncementRepositoryImpl(
        announcementLocalDataSource = announcementLocalDataSource,
        announcementRemoteDataSource = announcementRemoteDataSource,
        database = database,
    )

    @Provides
    @Singleton
    fun provideTagsRepository(
        announcementTagsLocalDataSource: TagsDao,
        tagsLocalDataSource: TagsLocalDataSource,
        tagsRemoteDataSource: TagsRemoteDataSource,

    ): TagsRepository = TagsRepositoryImpl(
        subscribableTagsLocalDataSource = tagsLocalDataSource,
        announcementTagsLocalDataSource = announcementTagsLocalDataSource,
        tagsRemoteDataSource = tagsRemoteDataSource,
    )

    @Provides
    @Singleton
    fun provideAuthorRepository(
        authorLocalDataSource: AuthorsDao,
        authorRemoteDataSource: AuthorRemoteDataSource,
    ): AuthorRepository = AuthorRepositoryImpl(
        authorLocalDataSource = authorLocalDataSource,
        authorRemoteDataSource = authorRemoteDataSource
    )

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        authenticationLocalDataSource: AuthenticationLocalDataSource,
        authenticationRemoteDataSource: AuthenticationRemoteDataSource
    ): AuthenticationRepository = AuthenticationRepositoryImpl(
        authenticationLocalDataSource = authenticationLocalDataSource,
        authenticationRemoteDataSource = authenticationRemoteDataSource
    )

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        preferencesLocalDataSource: PreferencesLocalDataSource
    ): UserPreferencesRepository =
        UserPreferencesRepoImpl(preferencesLocalDataSource = preferencesLocalDataSource)

    @Provides
    @Singleton
    fun provideUserInfoRepository(
        profileLocalDataSource: ProfileLocalDataSource,
        profileRemoteDataSource: ProfileRemoteDataSource,
        pushNotificationsDatasource: PushNotificationsDatasource
    ): ProfileRepository = ProfileRepositoryImpl(
        profileLocalDataSource = profileLocalDataSource,
        profileRemoteDataSource = profileRemoteDataSource,
        pushNotificationsDatasource = pushNotificationsDatasource
    )

    @Provides
    @Singleton
    fun provideAppCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

}

