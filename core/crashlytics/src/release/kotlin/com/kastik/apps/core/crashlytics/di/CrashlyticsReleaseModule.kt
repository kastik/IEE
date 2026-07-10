package com.kastik.apps.core.crashlytics.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.crashlytics.CrashlyticsReleaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashlyticsReleaseModule {

    @Binds
    @Singleton
    abstract fun bindCrashlytics(impl: CrashlyticsReleaseImpl): Crashlytics

    companion object {

        @Provides
        @Singleton
        fun provideFirebaseCrashlytics(
        ): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    }

}