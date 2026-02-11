package com.kastik.apps.core.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kastik.apps.core.domain.repository.RemoteConfigRepository
import com.kastik.apps.core.domain.service.Notifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notifier: Notifier

    @Inject
    lateinit var config: RemoteConfigRepository

    companion object {

        private val recentIds = mutableSetOf<String>()
        private val lock = Any()
    }

    override fun onNewToken(token: String) = Unit

    override fun onMessageReceived(message: RemoteMessage) {

        if (!config.isFcmEnabled()) {
            return
        }

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
            notifier.sendPushNotification(announcementId.toInt(), title, body)
        }
    }
}