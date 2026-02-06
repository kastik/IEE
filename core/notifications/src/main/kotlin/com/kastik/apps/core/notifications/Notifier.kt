package com.kastik.apps.core.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.kastik.apps.core.common.di.MainDispatcher
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.notifications.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class NotifierImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : Notifier {
    private val notificationManager by lazy {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override suspend fun sendPushNotification(title: String) {
        sendPushNotification("IEE", title)
    }

    override suspend fun sendPushNotification(title: String, body: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.fcm_announcement_channel_id)
        ).apply {
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        notificationManager.notify(Random.nextInt(), builder.build())
    }

    override suspend fun sendAnnouncementNotification(announcement: Announcement) {

        val deepLinkUri = "com.kastik.apps://announcement?id=${announcement.id}".toUri()


        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val builder =
            NotificationCompat.Builder(
                context,
                context.getString(R.string.fcm_announcement_channel_id)
            )
                .apply {
                    setSmallIcon(R.drawable.ic_notification_icon)
                    setContentTitle(announcement.title)
                    setContentText(announcement.body)
                    setAutoCancel(true)
                    setContentIntent(pendingIntent)
                }

        notificationManager.notify(announcement.id, builder.build())
    }

    override suspend fun sendToastNotification(message: String) {
        withContext(mainDispatcher) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun sendToastNotification(@StringRes resId: Int) {
        withContext(mainDispatcher) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
        }
    }
}