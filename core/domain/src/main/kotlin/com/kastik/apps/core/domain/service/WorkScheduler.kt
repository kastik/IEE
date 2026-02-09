package com.kastik.apps.core.domain.service


interface WorkScheduler {
    fun scheduleTokenRefresh()
    fun cancelTokenRefresh()
    fun scheduleAnnouncementAlerts()
    fun cancelAnnouncementAlerts()
}