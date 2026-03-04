package com.kastik.apps.core.domain.service

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow


interface WorkScheduler {
    fun scheduleAnnouncementAlerts(intervalMinutes: Long)
    fun getAnnouncementAlertsWorkInfo(): Flow<WorkInfo?>
    fun cancelAnnouncementAlerts()
}