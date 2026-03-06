package com.kastik.apps.core.di

import android.content.Context
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
    internal abstract fun bindAnalytics(impl: AnalyticsReleaseImpl): Analytics
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseAnalyticsProviderModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
}
