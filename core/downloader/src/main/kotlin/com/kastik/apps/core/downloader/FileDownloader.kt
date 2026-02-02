package com.kastik.apps.core.downloader

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.kastik.apps.core.domain.service.FileDownloader

internal class FileDownloaderImpl(
    context: Context
) : FileDownloader {
    private val downloadManger = context.getSystemService(DownloadManager::class.java)

    override fun downloadAttachment(
        url: String,
        fileName: String,
        mimeType: String,
        authToken: String?
    ) {

        val request = DownloadManager.Request(url.toUri()).apply {
            setTitle(fileName)
            setMimeType(mimeType)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            if (authToken != null) {
                addRequestHeader("Authorization", "Bearer $authToken")
            }
        }
        downloadManger.enqueue(request)
    }
}