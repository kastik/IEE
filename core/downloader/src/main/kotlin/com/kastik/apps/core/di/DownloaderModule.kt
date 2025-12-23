package com.kastik.apps.core.di

import android.content.Context
import com.kastik.apps.core.domain.service.FileDownloader
import com.kastik.apps.core.downloader.FileDownloaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DownloaderModule {

    @Provides
    @Singleton
    fun provideFileDownloader(
        @ApplicationContext context: Context,
    ): FileDownloader = FileDownloaderImpl(context)
}

