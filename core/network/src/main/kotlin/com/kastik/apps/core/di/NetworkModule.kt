package com.kastik.apps.core.di

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSourceImpl
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSourceImpl
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSource
import com.kastik.apps.core.network.datasource.AuthorRemoteDataSourceImpl
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSource
import com.kastik.apps.core.network.datasource.ProfileRemoteDataSourceImpl
import com.kastik.apps.core.network.datasource.TagsRemoteDataSource
import com.kastik.apps.core.network.datasource.TagsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            isLenient = true
        }



        @Provides
        @Singleton
        fun provideAboardApi(@AboardRetrofit retrofit: Retrofit): AboardApiClient =
            retrofit.create(AboardApiClient::class.java)
    }

    @Binds
    @Singleton
    internal abstract fun bindAuthenticationRemoteDataSource(
        authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl
    ): AuthenticationRemoteDataSource

    @Binds
    @Singleton
    internal abstract fun bindTagsRemoteDataSource(
        tagsRemoteDataSourceImpl: TagsRemoteDataSourceImpl
    ): TagsRemoteDataSource

    @Binds
    @Singleton
    internal abstract fun bindAuthorRemoteDataSource(
        authorRemoteDataSourceImpl: AuthorRemoteDataSourceImpl
    ): AuthorRemoteDataSource

    @Binds
    @Singleton
    internal abstract fun bindProfileRemoteDataSource(
        profileRemoteDataSourceImpl: ProfileRemoteDataSourceImpl
    ): ProfileRemoteDataSource

    @Binds
    @Singleton
    internal abstract fun bindAnnouncementRemoteDataSource(
        announcementRemoteDataSourceImpl: AnnouncementRemoteDataSourceImpl
    ): AnnouncementRemoteDataSource


}