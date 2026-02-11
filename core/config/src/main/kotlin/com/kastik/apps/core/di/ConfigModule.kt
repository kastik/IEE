package com.kastik.apps.core.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.kastik.apps.core.config.FirebaseConfigRepository
import com.kastik.apps.core.config.R
import com.kastik.apps.core.domain.repository.RemoteConfigRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigModule {

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
            val config = Firebase.remoteConfig
            val settings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            config.setConfigSettingsAsync(settings)
            config.setDefaultsAsync(R.xml.remote_config_defaults)
            return config
        }
    }

    @Binds
    internal abstract fun bindRemoteConfig(impl: FirebaseConfigRepository): RemoteConfigRepository

}