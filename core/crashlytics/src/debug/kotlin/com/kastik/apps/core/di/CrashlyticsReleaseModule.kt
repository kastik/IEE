package com.kastik.apps.core.di

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
abstract class CrashlyticsModule {
    @Binds
    @Singleton
    internal abstract fun bindCrashlytics(impl: CrashlyticsDebugImpl): Crashlytics
}