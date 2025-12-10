package com.kastik.apps.core.di

import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsDebugImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsDebugModule {

    @Binds
    @Singleton
    internal abstract fun bindAnalytics(impl: AnalyticsDebugImpl): Analytics
}