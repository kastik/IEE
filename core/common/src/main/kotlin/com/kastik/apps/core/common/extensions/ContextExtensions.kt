package com.kastik.apps.core.common.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

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

fun Context.launchSignIn() {
    //TODO Shouldn't hard code this, move it to gradle at some point
    val url = "https://login.iee.ihu.gr/authorization?" +
            "client_id=690a9861468c9b767cabdc40" + "&response_type=code" +
            "&scope=announcements,profile" +
            "&redirect_uri=com.kastik.apps://auth"

    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}


fun Context.launchUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}