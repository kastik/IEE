package com.kastik.apps.core.di

import com.kastik.apps.core.network.interceptor.AboardAuthenticator
import com.kastik.apps.core.network.interceptor.TokenInterceptor
import com.kastik.apps.core.network.serializers.SortTypeQueryConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModuleProduction {

    @Provides
    @Singleton
    @AuthenticatorAboardRetrofit
    fun provideAuthenticatorAboardRetrofit(
        @AuthenticatorAboardOkHttp client: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://aboard.iee.ihu.gr/api/v2/")
            .addConverterFactory(SortTypeQueryConverterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @BaseAboardRetrofit
    fun provideBaseAboardRetrofit(
        @BaseAboardOkHttp client: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://aboard.iee.ihu.gr/api/v2/")
            .addConverterFactory(SortTypeQueryConverterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @AuthenticatorAboardOkHttp
    fun provideAuthenticatorAboardOkHttp(
        tokenInterceptor: TokenInterceptor,
        aboardAuthenticator: AboardAuthenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .authenticator(aboardAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    @BaseAboardOkHttp
    fun provideBaseAboardOkHttp(
        tokenInterceptor: TokenInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .build()
    }

}