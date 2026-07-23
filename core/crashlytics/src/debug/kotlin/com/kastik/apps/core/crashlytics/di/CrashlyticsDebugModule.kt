package com.kastik.apps.core.crashlytics.di

import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.crashlytics.CrashlyticsDebugImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashlyticsModule {
    @Binds @Singleton abstract fun bindCrashlytics(impl: CrashlyticsDebugImpl): Crashlytics
}
