package com.kastik.apps.core.config.di

import com.kastik.apps.core.config.RemoteConfig
import com.kastik.apps.core.config.RemoteConfigDebugImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RemoteConfigDebugModule {

    @Binds @Singleton fun bindRemoteConfig(impl: RemoteConfigDebugImpl): RemoteConfig
}
