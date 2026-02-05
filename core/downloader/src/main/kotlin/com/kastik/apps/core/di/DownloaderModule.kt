package com.kastik.apps.core.di

import com.kastik.apps.core.domain.service.FileDownloader
import com.kastik.apps.core.downloader.FileDownloaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DownloaderModule {

    @Binds
    @Singleton
    internal abstract fun provideFileDownloader(
        fileDownloaderImpl: FileDownloaderImpl
    ): FileDownloader
}