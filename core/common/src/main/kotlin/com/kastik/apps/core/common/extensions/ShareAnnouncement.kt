package com.kastik.apps.core.common.extensions

import android.content.Context
import android.content.Intent

fun Context.shareAnnouncement(announcementId: Int) {
    val url = "https://aboard.iee.ihu.gr/announcements/$announcementId"
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Check out this announcement!")
    }
    startActivity(Intent.createChooser(sendIntent, "Share Announcement via"))
}