package com.kastik.apps.core.domain.service

interface Notifier {
    fun sendPushNotification(title: String)
    fun sendPushNotification(title: String, body: String?)
    fun sendPushNotification(announcementId: Int, title: String, body: String?)
    suspend fun sendToastNotification(message: String)
    suspend fun sendToastNotification(resId: Int)
}