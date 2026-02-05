package com.kastik.apps.core.downloader

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.kastik.apps.core.domain.service.FileDownloader
import com.kastik.apps.core.network.interceptor.TokenProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class FileDownloaderImpl @Inject constructor(
    @ApplicationContext context: Context,
    val tokenProvider: TokenProvider,
) : FileDownloader {
    private val downloadManger = context.getSystemService(DownloadManager::class.java)

    override fun downloadAttachment(
        url: String,
        fileName: String,
        mimeType: String,
    ) {
        val token = tokenProvider.token.value

        val request = DownloadManager.Request(url.toUri()).apply {
            setTitle(fileName)
            setMimeType(mimeType)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            if (token != null) {
                addRequestHeader("Authorization", "Bearer $token")
            }
        }
        downloadManger.enqueue(request)
    }
}