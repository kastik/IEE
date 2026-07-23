package com.kastik.apps.core.domain.service

import com.kastik.apps.core.model.sync.SyncState
import kotlinx.coroutines.flow.Flow

interface WorkScheduler {
    fun scheduleStartupSync()

    fun scheduleSubscribeToTags(ids: List<Int>)

    fun scheduleAnnouncementSync(announcementId: Int)

    fun scheduleAnnouncementAlerts(intervalMinutes: Int)

    val startupSyncState: Flow<SyncState>
    val subscribeToTagsSyncState: Flow<SyncState>
    val announcementSyncState: Flow<SyncState>
    val announcementAlertsSyncState: Flow<SyncState>

    fun cancelStartupSync()

    fun cancelSubscribeToTags()

    fun cancelAnnouncementSync()

    fun cancelAnnouncementAlerts()
}
