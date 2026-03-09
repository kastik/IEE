package com.kastik.apps.core.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kastik.apps.core.domain.service.Notifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notifier: Notifier

    override fun onNewToken(token: String) = Unit

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data["type"] == "fcm_campaign") {

            val title = message.data["title"] ?: message.notification?.title
            val body = message.data["body"] ?: message.notification?.body
            val uri = message.data["uri"] ?: message.data["link"]
            ?: message.notification?.link?.toString()

            if (title != null) {
                notifier.sendCampaignNotification(
                    title = title,
                    body = body,
                    uri = uri
                )
            }
        }
    }
}