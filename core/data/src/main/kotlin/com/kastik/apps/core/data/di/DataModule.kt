package com.kastik.apps.core.data.di

import com.kastik.apps.core.data.provider.AboardTokenManagerImpl
import com.kastik.apps.core.data.repository.AnnouncementRepositoryImpl
import com.kastik.apps.core.data.repository.AuthenticationRepositoryImpl
import com.kastik.apps.core.data.repository.AuthorRepositoryImpl
import com.kastik.apps.core.data.repository.NotificationsRepositoryImpl
import com.kastik.apps.core.data.repository.OnboardRepositoryImpl
import com.kastik.apps.core.data.repository.ProfileRepositoryImpl
import com.kastik.apps.core.data.repository.TagsRepositoryImpl
import com.kastik.apps.core.data.repository.UserPreferencesRepositoryImpl
import com.kastik.apps.core.data.utils.Base64ImageExtractor
import com.kastik.apps.core.data.utils.Base64ImageExtractorImpl
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.domain.repository.NotificationRepository
import com.kastik.apps.core.domain.repository.OnboardRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.network.interceptor.TokenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAnnouncementRepository(
        announcementRepositoryImpl: AnnouncementRepositoryImpl
    ): AnnouncementRepository

    @Binds
    @Singleton
    abstract fun bindTagsRepository(
        tagsRepositoryImpl: TagsRepositoryImpl
    ): TagsRepository

    @Binds
    @Singleton
    abstract fun bindAuthorRepository(
        authorRepositoryImpl: AuthorRepositoryImpl
    ): AuthorRepository


    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindUserInfoRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImp: NotificationsRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    internal abstract fun bindTokenProvider(
        tokenProvider: AboardTokenManagerImpl
    ): TokenManager

    @Binds
    @Singleton
    abstract fun bindBase64ImageExtractor(
        imageExtractorImpl: Base64ImageExtractorImpl
    ): Base64ImageExtractor

}