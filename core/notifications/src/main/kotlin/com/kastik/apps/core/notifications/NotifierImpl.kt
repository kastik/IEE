package com.kastik.apps.core.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.kastik.apps.core.notifications.NotificationIds.BASE_ANNOUNCEMENT_NOTIFICATION_ID
import com.kastik.apps.core.notifications.NotificationIds.CAMPAIGN_ANNOUNCEMENT_NOTIFICATION_ID
import com.kastik.apps.core.notifications.NotificationIds.GENERAL_ANNOUNCEMENT_NOTIFICATION_ID
import com.kastik.apps.notifications.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotifierImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {
    private val notificationManager by lazy {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannels(manager)
        manager
    }

    override fun sendGeneralNotification(
        @StringRes titleResId: Int, @StringRes bodyResId: Int?, @StringRes uri: Int?
    ) {
        val intent = if (uri != null) {
            Intent(Intent.ACTION_VIEW, context.getString(uri).toUri())
        } else {
            context.packageManager.getLaunchIntentForPackage(context.packageName)
        }?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(
            context, context.getString(R.string.channel_id_general)
        ).apply {
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(context.getString(titleResId))
            bodyResId.let { if (it != null) setContentText(context.getString(it)) }
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        notificationManager.notify(GENERAL_ANNOUNCEMENT_NOTIFICATION_ID, builder.build())
    }

    override fun sendCampaignNotification(
        title: String, body: String?, uri: String?
    ) {

        val deepLinkUri = uri?.toUri()

        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val builder = NotificationCompat.Builder(
            context, context.getString(R.string.channel_id_fcm_campaigns)
        ).apply {
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        notificationManager.notify(CAMPAIGN_ANNOUNCEMENT_NOTIFICATION_ID, builder.build())
    }

    override fun sendAnnouncementNotification(
        announcementId: Int, title: String, body: String
    ) {

        val deepLinkUri = "com.kastik.apps://announcement/$announcementId".toUri()


        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            announcementId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(
            context, context.getString(R.string.channel_id_announcements)
        ).apply {
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        notificationManager.notify(
            BASE_ANNOUNCEMENT_NOTIFICATION_ID + announcementId,
            builder.build()
        )
    }

    private fun createNotificationChannels(manager: NotificationManager) {

        val importantGroup = NotificationChannelGroup(
            context.getString(R.string.group_id_important),
            context.getString(R.string.group_name_important),
        )
        val campaignsGroup = NotificationChannelGroup(
            context.getString(R.string.group_id_campaigns),
            context.getString(R.string.group_name_campaigns)
        )

        manager.createNotificationChannelGroups(listOf(importantGroup, campaignsGroup))

        val syncChannel = NotificationChannel(
            context.getString(R.string.channel_id_sync),
            context.getString(R.string.channel_name_sync),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = context.getString(R.string.channel_desc_sync)
            group = context.getString(R.string.group_id_important)
        }

        val announcementChannel = NotificationChannel(
            context.getString(R.string.channel_id_announcements),
            context.getString(R.string.channel_name_announcements),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.channel_desc_announcements)
            group = context.getString(R.string.group_id_important)
        }

        val fcmCampaignChannel = NotificationChannel(
            context.getString(R.string.channel_id_fcm_campaigns),
            context.getString(R.string.channel_name_fcm_campaigns),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.channel_desc_fcm_campaigns)
            group = context.getString(R.string.group_id_campaigns)
        }

        val generalChannel = NotificationChannel(
            context.getString(R.string.channel_id_general),
            context.getString(R.string.channel_name_general),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.channel_desc_general)
            group = context.getString(R.string.group_id_important)
        }

        manager.createNotificationChannel(syncChannel)
        manager.createNotificationChannel(announcementChannel)
        manager.createNotificationChannel(fcmCampaignChannel)
        manager.createNotificationChannel(generalChannel)
    }

    override fun createSyncNotification(
        @StringRes titleResId: Int,
        @StringRes bodyResId: Int
    ): Notification {
        // We need to ensure the channel exists before returning the notification
        // (notificationManager lazy init handles this in your current setup)
        notificationManager // triggers the lazy block

        return NotificationCompat.Builder(
            context, context.getString(R.string.channel_id_sync)
        ).apply {
            setPriority(NotificationCompat.PRIORITY_LOW)
            setSmallIcon(R.drawable.ic_notification_icon)
            setContentTitle(context.getString(titleResId))
            setContentText(context.getString(bodyResId))
            setOngoing(true)
        }.build()
    }
}