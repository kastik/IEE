package com.kastik.apps.core.di

import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.work.scheduler.WorkSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkModule {

    @Binds
    abstract fun bindTokenRefreshScheduler(
        workSchedulerImpl: WorkSchedulerImpl
    ): WorkScheduler

}