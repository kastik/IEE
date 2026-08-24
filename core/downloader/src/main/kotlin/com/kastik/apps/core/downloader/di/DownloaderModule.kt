package com.kastik.apps.core.downloader.di

import com.kastik.apps.core.domain.service.FileDownloader
import com.kastik.apps.core.downloader.FileDownloaderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DownloaderModule {

    @Binds
    @Singleton
    fun provideFileDownloader(fileDownloaderImpl: FileDownloaderImpl): FileDownloader
}
