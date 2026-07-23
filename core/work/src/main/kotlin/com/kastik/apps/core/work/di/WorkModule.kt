package com.kastik.apps.core.work.di

import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.work.scheduler.WorkSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WorkModule {

    @Binds
    @Singleton
    abstract fun bindTokenRefreshScheduler(workSchedulerImpl: WorkSchedulerImpl): WorkScheduler
}
