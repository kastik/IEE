package com.kastik.apps.core.di

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.domain.usecases.CheckIfUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.CheckIfUserIsAuthenticatedUseCase
import com.kastik.apps.core.domain.usecases.ExchangeCodeForAboardTokenUseCase
import com.kastik.apps.core.domain.usecases.ExchangeCodeForAppsTokenUseCase
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.domain.usecases.GetAuthorsUseCase
import com.kastik.apps.core.domain.usecases.GetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.GetPagedAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetPagedFilteredAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetTagsUseCase
import com.kastik.apps.core.domain.usecases.GetUserProfileUseCase
import com.kastik.apps.core.domain.usecases.GetUserSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.GetUserThemeUseCase
import com.kastik.apps.core.domain.usecases.SetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.SetUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.SetUserThemeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AnnouncementUseCaseModule {


    @Provides
    @ViewModelScoped
    fun provideGetPagedAnnouncementsUseCase(
        repo: AnnouncementRepository
    ): GetPagedAnnouncementsUseCase = GetPagedAnnouncementsUseCase(repo)


    @Provides
    @ViewModelScoped
    fun provideGetPagedFilteredAnnouncementsUseCase(
        repo: AnnouncementRepository
    ): GetPagedFilteredAnnouncementsUseCase = GetPagedFilteredAnnouncementsUseCase(repo)



    @Provides
    @ViewModelScoped
    fun provideGetAnnouncementWithIdUseCase(
        repo: AnnouncementRepository
    ): GetAnnouncementWithIdUseCase = GetAnnouncementWithIdUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideGetTagsUseCase(
        repo: AnnouncementRepository
    ): GetTagsUseCase = GetTagsUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideGetAuthorsUseCase(
        repo: AnnouncementRepository
    ): GetAuthorsUseCase = GetAuthorsUseCase(repo)


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


    @Provides
    @ViewModelScoped
    fun provideGetUserThemeUseCase(
        repository: UserPreferencesRepository
    ): GetUserThemeUseCase = GetUserThemeUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSetUserThemeUseCase(
        repository: UserPreferencesRepository
    ): SetUserThemeUseCase = SetUserThemeUseCase(repository)


    @Provides
    @ViewModelScoped
    fun provideGetDynamicColorUseCase(
        repository: UserPreferencesRepository
    ): GetDynamicColorUseCase = GetDynamicColorUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSetDynamicColorUseCase(
        repository: UserPreferencesRepository
    ): SetDynamicColorUseCase = SetDynamicColorUseCase(repository)


}