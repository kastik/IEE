package com.kastik.apps.core.domain.service

interface Notifier {
    fun sendGeneralNotification(title: String, body: String? = null, uri: String? = null)
    fun sendGeneralNotification(titleResId: Int, bodyResId: Int? = null, uri: Int? = null)
    fun sendCampaignNotification(title: String, body: String? = null, uri: String? = null)
    fun sendAnnouncementNotification(announcementId: Int, title: String, body: String)

    suspend fun sendToastNotification(message: String)
    suspend fun sendToastNotification(resId: Int)
}