package com.kastik.apps.core.domain.service

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow


interface WorkScheduler {
    fun scheduleTokenRefresh()
    fun getTokenRefreshWorkInfo(): Flow<WorkInfo?>
    fun cancelTokenRefresh()
    fun scheduleAnnouncementAlerts()
    fun getAnnouncementAlertsWorkInfo(): Flow<WorkInfo?>
    fun cancelAnnouncementAlerts()
    fun scheduleSubscribeToTags(newTagIds: List<Int>)
    fun getSubscribeToTagsWorkInfo(): Flow<WorkInfo?>
    fun cancelSubscribeToTags()
    fun scheduleTopicsSync()
    fun getTopicsSyncWorkInfo(): Flow<WorkInfo?>
    fun cancelTopicsSync()
}