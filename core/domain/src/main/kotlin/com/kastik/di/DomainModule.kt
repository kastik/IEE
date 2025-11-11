package com.kastik.di

import com.kastik.repository.AnnouncementRepository
import com.kastik.repository.AuthenticationRepository
import com.kastik.repository.UserPreferencesRepository
import com.kastik.usecases.CheckIfUserHasSkippedSignInUseCase
import com.kastik.usecases.CheckIfUserIsAuthenticatedUseCase
import com.kastik.usecases.ExchangeCodeForAboardTokenUseCase
import com.kastik.usecases.ExchangeCodeForAppsTokenUseCase
import com.kastik.usecases.GetAnnouncementWithIdUseCase
import com.kastik.usecases.GetAuthorsUseCase
import com.kastik.usecases.GetPagedAnnouncementsUseCase
import com.kastik.usecases.GetUserProfileUseCase
import com.kastik.usecases.GetUserSubscriptionsUseCase
import com.kastik.usecases.SetUserHasSkippedSignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AnnouncementUseCaseModule {

    @Module
    @InstallIn(ViewModelComponent::class)
    object AnnouncementUseCaseModule {

        @Provides
        @ViewModelScoped
        fun provideGetPagedAnnouncementsUseCase(
            repo: AnnouncementRepository
        ): GetPagedAnnouncementsUseCase = GetPagedAnnouncementsUseCase(repo)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAnnouncementWithIdUseCase(
        repo: AnnouncementRepository
    ): GetAnnouncementWithIdUseCase = GetAnnouncementWithIdUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideRequestAppsAccessTokenUseCase(
        repository: AuthenticationRepository
    ): ExchangeCodeForAppsTokenUseCase = ExchangeCodeForAppsTokenUseCase(repository)


    @Provides
    @ViewModelScoped
    fun provideRequestAboardAccessTokenUseCase(
        repository: AuthenticationRepository
    ): ExchangeCodeForAboardTokenUseCase = ExchangeCodeForAboardTokenUseCase(repository)


    @Provides
    @ViewModelScoped
    fun provideGetAuthorsUseCase(
        repository: AnnouncementRepository
    ): GetAuthorsUseCase = GetAuthorsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideCheckIfUserIsAuthenticatedUseCase(
        repository: AuthenticationRepository
    ): CheckIfUserIsAuthenticatedUseCase = CheckIfUserIsAuthenticatedUseCase(repository)


    @Provides
    @ViewModelScoped
    fun provideGetUserProfileUseCase(
        repository: AuthenticationRepository
    ): GetUserProfileUseCase = GetUserProfileUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetUserSubscriptionsUseCase(
        repository: AuthenticationRepository
    ): GetUserSubscriptionsUseCase = GetUserSubscriptionsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideCheckIfUserHasSkippedSignInUseCase(
        repository: UserPreferencesRepository
    ): CheckIfUserHasSkippedSignInUseCase = CheckIfUserHasSkippedSignInUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSetUserHasSkippedSignInUseCase(
        repository: UserPreferencesRepository
    ): SetUserHasSkippedSignInUseCase = SetUserHasSkippedSignInUseCase(repository)

}