package com.kastik.apps.core.di

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.api.AppsApiClient
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
import com.kastik.apps.core.network.interceptor.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
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

    @Provides
    @Singleton
    fun provideTagsRemoteDataSource(
        aboardApiClient: AboardApiClient
    ): TagsRemoteDataSource =
        TagsRemoteDataSourceImpl(aboardApiClient)

    @Provides
    @Singleton
    fun provideAuthorRemoteDataSource(
        aboardApiClient: AboardApiClient
    ): AuthorRemoteDataSource =
        AuthorRemoteDataSourceImpl(aboardApiClient)

    @Provides
    @Singleton
    fun provideAnnouncementRemoteDataSource(
        aboardApiClient: AboardApiClient
    ): AnnouncementRemoteDataSource =
        AnnouncementRemoteDataSourceImpl(aboardApiClient = aboardApiClient)

    @Provides
    @Singleton
    fun provideProfileRemoteDataSource(
        aboardApiClient: AboardApiClient
    ): ProfileRemoteDataSource = ProfileRemoteDataSourceImpl(aboardApiClient)


}

@Singleton
class TokenProvider @Inject constructor(
    appScope: CoroutineScope,
    localDataSource: AuthenticationLocalDataSource,
) {
    val token: StateFlow<String?> =
        localDataSource.getAboardAccessTokenFlow()
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    fun getToken(): String? = token.value
}