package com.kastik.appsaboard.di

import com.kastik.appsaboard.data.datasource.remote.api.ItApiClient
import com.kastik.appsaboard.data.datasource.remote.source.AnnouncementRemoteDataSource
import com.kastik.appsaboard.data.repository.AnnouncementRepoImpl
import com.kastik.appsaboard.domain.repository.AnnouncementRepository
import com.kastik.appsaboard.domain.usecases.announcements.GetPublicAnnouncementsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnnouncementNetworkModule {

    @Provides
    @Singleton
    fun provideAnnouncementApi(retrofit: Retrofit): ItApiClient =
        retrofit.create(ItApiClient::class.java)

    @Provides
    @Singleton
    fun provideAnnouncementRemoteDataSource(
        api: ItApiClient
    ): AnnouncementRemoteDataSource = AnnouncementRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideAnnouncementRepository(
        remoteDataSource: AnnouncementRemoteDataSource
    ): AnnouncementRepository = AnnouncementRepoImpl(remoteDataSource)
}

@Module
@InstallIn(ViewModelComponent::class)
object AnnouncementUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetAnnouncementsUseCase(
        repository: AnnouncementRepository
    ): GetPublicAnnouncementsUseCase = GetPublicAnnouncementsUseCase(repository)
}
