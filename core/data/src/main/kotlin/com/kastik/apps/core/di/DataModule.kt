package com.kastik.apps.core.di

import com.kastik.apps.core.data.repository.AnnouncementRepositoryImpl
import com.kastik.apps.core.data.repository.AuthenticationRepositoryImpl
import com.kastik.apps.core.data.repository.AuthorRepositoryImpl
import com.kastik.apps.core.data.repository.ProfileRepositoryImpl
import com.kastik.apps.core.data.repository.TagsRepositoryImpl
import com.kastik.apps.core.data.repository.UserPreferencesRepositoryImpl
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.AuthorRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.domain.repository.TagsRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindAnnouncementRepository(
        announcementRepositoryImpl: AnnouncementRepositoryImpl
    ): AnnouncementRepository

    @Binds
    internal abstract fun bindTagsRepository(
        tagsRepositoryImpl: TagsRepositoryImpl
    ): TagsRepository

    @Binds
    internal abstract fun bindAuthorRepository(
        authorRepositoryImpl: AuthorRepositoryImpl
    ): AuthorRepository


    @Binds
    internal abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    internal abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    @Binds
    internal abstract fun bindUserInfoRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

}

