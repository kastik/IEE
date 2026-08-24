package com.kastik.apps.core.domain.service

import com.kastik.apps.core.model.sync.SyncStatus
import kotlinx.coroutines.flow.Flow

interface WorkScheduler {
    fun scheduleStartupSync()

    fun scheduleSubscribeToTags(ids: List<Int>)

    fun scheduleAnnouncementSync(announcementId: Int)

    fun scheduleAnnouncementAlerts(intervalMinutes: Int)

    val startupSyncStatus: Flow<SyncStatus>
    val subscribeToTagsSyncStatus: Flow<SyncStatus>
    val announcementSyncStatus: Flow<SyncStatus>
    val announcementAlertsSyncStatus: Flow<SyncStatus>

    fun cancelStartupSync()

    fun cancelSubscribeToTags()

    fun cancelAnnouncementSync()

    fun cancelAnnouncementAlerts()
}
