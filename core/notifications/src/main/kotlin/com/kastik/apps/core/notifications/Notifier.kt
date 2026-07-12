package com.kastik.apps.core.notifications

import android.app.Notification
import androidx.annotation.StringRes

interface Notifier {
    fun sendGeneralNotification(
        @StringRes titleResId: Int,
        @StringRes bodyResId: Int? = null,
        uri: Int? = null
    )

    fun sendCampaignNotification(
        title: String,
        body: String? = null,
        uri: String? = null
    )

    fun sendAnnouncementNotification(
        announcementId: Int,
        title: String,
        body: String
    )

    fun createSyncNotification(
        @StringRes titleResId: Int,
        @StringRes bodyResId: Int,
    ): Notification
}