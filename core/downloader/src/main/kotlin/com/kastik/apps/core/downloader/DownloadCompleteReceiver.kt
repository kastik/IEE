package com.kastik.apps.core.downloader

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DownloadCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) {
                openDownloadedFile(context, id)
            }
        }
    }

    private fun openDownloadedFile(context: Context?, downloadId: Long) {
        val manager =
            context?.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager ?: return
        val uri = manager.getUriForDownloadedFile(downloadId)
        val mimeType =
            manager.getMimeTypeForDownloadedFile(downloadId) // Helper function you might need to write

        if (uri != null) {
            val openIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, mimeType)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(openIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "No app found to open this file", Toast.LENGTH_SHORT).show()
            }
        }
    }
}