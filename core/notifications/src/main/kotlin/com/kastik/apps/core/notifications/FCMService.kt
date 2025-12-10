package com.kastik.apps.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kastik.apps.notifications.R

internal class FCMService() : FirebaseMessagingService() {

    companion object {

        private val recentIds = mutableSetOf<String>()
        private val lock = Any()
    }

    override fun onNewToken(token: String) {
        Log.d("MyLog", "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {

        createNotificationChannels()

        if (message.data["type"] == "announcement_created") {
            val announcementId = message.data["announcement_id"] ?: return
            val title = message.data["title"] ?: return
            val body = message.data["body"]
            synchronized(lock) {
                if (recentIds.contains(announcementId)) {
                    return
                }
                recentIds.add(announcementId)
            }
            postAnnouncementNotification(announcementId, title, body)
        }
    }

    private fun postAnnouncementNotification(
        announcementId: String,
        title: String,
        body: String? = null,
    ) {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val deepLinkUri = "com.kastik.apps://announcement?id=$announcementId".toUri()


        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val builder =
            NotificationCompat.Builder(this, getString(R.string.fcm_announcement_channel_id))
                .apply {
                    setSmallIcon(R.drawable.ic_notification_icon)
                    setContentTitle(title)
                    setContentText(body)
                    setAutoCancel(true)
                    setContentIntent(pendingIntent)
                }

        val notificationId = announcementId.toIntOrNull() ?: System.currentTimeMillis().toInt()

        notificationManager.notify(notificationId, builder.build())
    }
}

private fun Context.createNotificationChannels() {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) return

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val announcementChannel = NotificationChannel(
        getString(R.string.fcm_announcement_channel_id),
        getString(R.string.fcm_announcement_channel_name),
        NotificationManager.IMPORTANCE_HIGH
    )
    val generalChannel = NotificationChannel(
        getString(R.string.fcm_default_channel_id),
        getString(R.string.fcm_default_channel_name),
        NotificationManager.IMPORTANCE_HIGH
    )

    notificationManager.createNotificationChannel(announcementChannel)
    notificationManager.createNotificationChannel(generalChannel)

}
