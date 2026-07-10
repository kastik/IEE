package com.kastik.apps.core.performance.di

import com.kastik.apps.core.performance.Performance
import com.kastik.apps.core.performance.PerformanceDebugImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PerformanceDebugModule {

    @Binds
    @Singleton
    abstract fun bindPerformanceDebugImpl(performanceDebugImpl: PerformanceDebugImpl): Performance


}