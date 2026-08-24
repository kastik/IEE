package com.kastik.apps.core.analytics.di

import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsDebugImpl
import com.kastik.apps.core.analytics.AnalyticsEventTypes
import com.kastik.apps.core.analytics.AnalyticsEventTypesDebugImpl
import com.kastik.apps.core.analytics.AnalyticsParamKeys
import com.kastik.apps.core.analytics.AnalyticsParamKeysDebugImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface AnalyticsDebugModule {

    @Binds
    @Singleton
    fun bindEventTypes(impl: AnalyticsEventTypesDebugImpl): AnalyticsEventTypes

    @Binds
    @Singleton
    fun bindParamKeys(impl: AnalyticsParamKeysDebugImpl): AnalyticsParamKeys

    @Binds @Singleton fun bindAnalytics(impl: AnalyticsDebugImpl): Analytics
}
