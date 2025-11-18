package com.kastik.apps.core.di

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.firebase.analytics.FirebaseAnalytics
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsReleaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {
    @Binds
    @Singleton
    abstract fun bindAnalytics(impl: AnalyticsReleaseImpl): Analytics
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseAnalyticsProviderModule {

    @RequiresPermission(allOf = [Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WAKE_LOCK])
    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
}
