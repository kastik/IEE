package com.kastik.apps.core.common.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.core.net.toUri
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory

//TODO Move these into gradle at some point
fun Context.launchSignIn() {
    val url = "https://login.iee.ihu.gr/authorization?" +
            "client_id=690a9861468c9b767cabdc40" + "&response_type=code" +
            "&scope=announcements,profile" +
            "&redirect_uri=com.kastik.apps://auth"
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

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

fun Context.launchUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

tailrec fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

suspend fun Context.launchInAppReview(
    onSuccessfulReview: () -> Unit
) {
    val activity = this.getActivity() ?: return

    val manager = ReviewManagerFactory.create(this)

    try {
        val reviewInfo = manager.requestReview()
        manager.launchReview(activity, reviewInfo)
        onSuccessfulReview()
    } catch (e: Exception) {
        return
    }
}