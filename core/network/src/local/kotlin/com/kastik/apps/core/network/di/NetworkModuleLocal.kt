package com.kastik.apps.core.network.di

import android.annotation.SuppressLint
import com.kastik.apps.core.network.constants.CONNECTION_TIMEOUT_SECONDS
import com.kastik.apps.core.network.constants.READ_TIMEOUT_SECONDS
import com.kastik.apps.core.network.interceptor.AboardAuthenticator
import com.kastik.apps.core.network.interceptor.TokenInterceptor
import com.kastik.apps.core.network.serializers.SortTypeConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
@SuppressLint("CustomX509TrustManager", "TrustAllX509TrustManager")
internal interface NetworkModuleLocal {
    @Provides
    @Singleton
    @AuthenticatorAboardRetrofit
    fun provideAuthenticatorAboardRetrofit(
        @AuthenticatorAboardOkHttp client: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://kastik-pc.local/api/")
            .addConverterFactory(SortTypeConverterFactory())
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @BaseAboardRetrofit
    fun provideBaseAboardRetrofit(
        @BaseAboardOkHttp client: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://kastik-pc.local/api/")
            .addConverterFactory(SortTypeConverterFactory())
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
        val logger =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {}

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .authenticator(aboardAuthenticator)
            .addNetworkInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    @BaseAboardOkHttp
    fun provideBaseAboardOkHttp(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val logger =
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {}

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addNetworkInterceptor(logger)
            .build()
    }
}
