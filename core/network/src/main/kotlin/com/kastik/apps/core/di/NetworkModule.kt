package com.kastik.apps.core.di

import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.api.AppsApiClient
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSourceImpl
import com.kastik.apps.core.network.interceptor.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    @Provides
    @Singleton
    @AuthOkHttp
    fun provideAuthOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .build()
    }

    //TODO Remove logging from release builds
    @Provides
    @Singleton
    @AnnOkHttp
    fun provideAnnOkHttp(
        tokenInterceptor: TokenInterceptor
    ): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(logger)
            .build()
    }

    //TODO Remove logging from release builds
    @Provides
    @Singleton
    @AuthRetrofit
    fun provideAuthRetrofit(
        @AuthOkHttp client: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://login.iee.ihu.gr/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @AnnRetrofit
    fun provideAnnRetrofit(
        @AnnOkHttp client: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://aboard.iee.ihu.gr/api/v2/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationApi(@AuthRetrofit retrofit: Retrofit): AppsApiClient =
        retrofit.create(AppsApiClient::class.java)

    @Provides
    @Singleton
    fun provideAnnouncementApi(@AnnRetrofit retrofit: Retrofit): AboardApiClient =
        retrofit.create(AboardApiClient::class.java)

    @Provides
    @Singleton
    fun provideAuthenticationRemoteDataSource(
        appsApiClient: AppsApiClient,
        aboardApiClient: AboardApiClient
    ): AuthenticationRemoteDataSource =
        AuthenticationRemoteDataSourceImpl(appsApiClient, aboardApiClient)
}