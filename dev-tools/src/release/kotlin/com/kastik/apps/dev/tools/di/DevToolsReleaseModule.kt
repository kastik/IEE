package com.kastik.apps.dev.tools.di

import com.kastik.apps.core.dev.tools.DevTools
import com.kastik.apps.dev.tools.DevToolsReleaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DevToolsReleaseModule {
    @Binds @Singleton abstract fun bindDevTools(devToolsReleaseImpl: DevToolsReleaseImpl): DevTools
}
