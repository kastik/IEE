package com.kastik.apps.core.downloader

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.kastik.apps.core.domain.service.FileDownloader
import com.kastik.apps.core.network.interceptor.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


//TODO DownloadManager doesn't support authenticators and can result in race conditions,
// convert this to a Retrofit api call at some point

internal class FileDownloaderImpl @Inject constructor(
    @ApplicationContext context: Context,
    val tokenManager: TokenManager,
) : FileDownloader {
    private val downloadManger = context.getSystemService(DownloadManager::class.java)

    override suspend fun downloadAttachment(
        url: String,
        fileName: String,
        mimeType: String,
    ) {
        val token = tokenManager.getToken()

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