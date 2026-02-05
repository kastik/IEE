package com.kastik.apps.core.domain.service

import com.kastik.apps.core.model.aboard.Announcement

interface Notifier {
    suspend fun sendPushNotification(title: String)
    suspend fun sendPushNotification(title: String, body: String)
    suspend fun sendAnnouncementNotification(announcement: Announcement)
    suspend fun sendToastNotification(message: String)
}