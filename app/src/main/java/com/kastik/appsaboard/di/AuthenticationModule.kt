package com.kastik.appsaboard.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kastik.appsaboard.data.datasource.local.AuthenticationLocalDataSource
import com.kastik.appsaboard.data.datasource.remote.api.AuthApiClient
import com.kastik.appsaboard.data.datasource.remote.source.AuthenticationRemoteDataSource
import com.kastik.appsaboard.data.repository.AuthenticationRepositoryImpl
import com.kastik.appsaboard.domain.repository.AuthenticationRepository
import com.kastik.appsaboard.domain.usecases.ExchangeCodeForTokenUseCase
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
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideAuthenticationApi(@AuthRetrofit retrofit: Retrofit): AuthApiClient =
        retrofit.create(AuthApiClient::class.java)

    @Provides
    @Singleton
    fun provideAuthenticationRemoteDataSource(
        api: AuthApiClient
    ): AuthenticationRemoteDataSource = AuthenticationRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideAuthenticationLocalDataSource(
        dataStore: DataStore<Preferences>
    ): AuthenticationLocalDataSource = AuthenticationLocalDataSource(dataStore)

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        local: AuthenticationLocalDataSource,
        remote: AuthenticationRemoteDataSource
    ): AuthenticationRepository = AuthenticationRepositoryImpl(local, remote)
}

@Module
@InstallIn(ViewModelComponent::class)
object AuthenticationUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideRequestAccessTokenUseCase(
        repository: AuthenticationRepository
    ): ExchangeCodeForTokenUseCase = ExchangeCodeForTokenUseCase(repository)
}
