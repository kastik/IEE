package com.kastik.apps.di

import com.kastik.apps.core.dev.tools.DevTools
import com.kastik.apps.dev.tools.DevToolsDebugImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DevToolsDebugModule {

    @Binds
    @Singleton
    internal abstract fun bindDevTools(
        devToolsDebugImpl: DevToolsDebugImpl
    ): DevTools

}