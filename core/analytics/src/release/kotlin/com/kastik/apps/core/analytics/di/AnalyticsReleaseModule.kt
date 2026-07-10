package com.kastik.apps.core.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsEventTypes
import com.kastik.apps.core.analytics.AnalyticsEventTypesReleaseImpl
import com.kastik.apps.core.analytics.AnalyticsParamKeys
import com.kastik.apps.core.analytics.AnalyticsParamKeysReleaseImpl
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
internal abstract class AnalyticsModule {

    @Binds
    @Singleton
    abstract fun bindEventTypes(impl: AnalyticsEventTypesReleaseImpl): AnalyticsEventTypes

    @Binds
    @Singleton
    abstract fun bindParamKeys(impl: AnalyticsParamKeysReleaseImpl): AnalyticsParamKeys

    @Binds
    @Singleton
    abstract fun bindAnalytics(impl: AnalyticsReleaseImpl): Analytics


    companion object {

        @Provides
        @Singleton
        fun provideFirebaseAnalytics(
            @ApplicationContext context: Context
        ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    }
}
