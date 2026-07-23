package com.kastik.apps.core.work.scheduler

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.sync.SyncState
import com.kastik.apps.core.work.worker.AnnouncementAlertWorker
import com.kastik.apps.core.work.worker.AnnouncementSyncWorker
import com.kastik.apps.core.work.worker.AnnouncementSyncWorker.Companion.KEY_ANNOUNCEMENT_ID
import com.kastik.apps.core.work.worker.StartupSyncWorker
import com.kastik.apps.core.work.worker.SubscribeWorker
import com.kastik.apps.core.work.worker.SubscribeWorker.Companion.KEY_TAG_IDS
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
internal class WorkSchedulerImpl @Inject constructor(@ApplicationContext context: Context) :
    WorkScheduler {

    companion object {
        const val STARTUP_SYNC_WORK_NAME = "STARTUP_SYNC_WORK"
        const val SUBSCRIBE_TO_TAGS_WORK_NAME = "SUBSCRIBE_TO_TAGS_WORK"
        const val ANNOUNCEMENT_SYNC_WORK_NAME = "ANNOUNCEMENT_SYNC_WORK"
        const val ANNOUNCEMENT_REFRESH_WORK_NAME = "ANNOUNCEMENT_REFRESH_WORK"
    }

    private val workManager = WorkManager.getInstance(context)

    override fun scheduleStartupSync() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val oneTimeSyncRequest =
            OneTimeWorkRequestBuilder<StartupSyncWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.EXPONENTIAL,
                    backoffDelay = WorkRequest.MIN_BACKOFF_MILLIS,
                    timeUnit = TimeUnit.MILLISECONDS,
                )
                .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName = STARTUP_SYNC_WORK_NAME,
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = oneTimeSyncRequest,
        )
    }

    override fun scheduleSubscribeToTags(ids: List<Int>) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val inputData = Data.Builder().putIntArray(KEY_TAG_IDS, ids.toIntArray()).build()

        val subscribeWorkRequest =
            OneTimeWorkRequestBuilder<SubscribeWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    backoffDelay = WorkRequest.MIN_BACKOFF_MILLIS,
                    timeUnit = TimeUnit.MILLISECONDS,
                )
                .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName = SUBSCRIBE_TO_TAGS_WORK_NAME,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = subscribeWorkRequest,
        )
    }

    override fun scheduleAnnouncementSync(announcementId: Int) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val inputData = Data.Builder().putInt(KEY_ANNOUNCEMENT_ID, announcementId).build()

        val syncAnnouncementWorkRequest =
            OneTimeWorkRequestBuilder<AnnouncementSyncWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    backoffDelay = WorkRequest.MIN_BACKOFF_MILLIS,
                    timeUnit = TimeUnit.MILLISECONDS,
                )
                .build()

        workManager.enqueueUniqueWork(
            uniqueWorkName = ANNOUNCEMENT_SYNC_WORK_NAME,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = syncAnnouncementWorkRequest,
        )
    }

    override fun scheduleAnnouncementAlerts(intervalMinutes: Int) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val announcementRefreshWorkRequest =
            PeriodicWorkRequestBuilder<AnnouncementAlertWorker>(
                    repeatInterval = intervalMinutes.toLong(),
                    repeatIntervalTimeUnit = TimeUnit.MINUTES,
                )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.LINEAR,
                    backoffDelay = WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                    TimeUnit.MILLISECONDS,
                )
                .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = ANNOUNCEMENT_REFRESH_WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.UPDATE,
            request = announcementRefreshWorkRequest,
        )
    }

    override val startupSyncState: Flow<SyncState> =
        workManager.getWorkInfosForUniqueWorkFlow(STARTUP_SYNC_WORK_NAME).mapToSyncState()

    override val subscribeToTagsSyncState: Flow<SyncState> =
        workManager.getWorkInfosForUniqueWorkFlow(SUBSCRIBE_TO_TAGS_WORK_NAME).mapToSyncState()

    override val announcementSyncState: Flow<SyncState> =
        workManager.getWorkInfosForUniqueWorkFlow(ANNOUNCEMENT_SYNC_WORK_NAME).mapToSyncState()

    override val announcementAlertsSyncState: Flow<SyncState> =
        workManager.getWorkInfosForUniqueWorkFlow(ANNOUNCEMENT_REFRESH_WORK_NAME).mapToSyncState()

    override fun cancelStartupSync() {
        workManager.cancelUniqueWork(uniqueWorkName = STARTUP_SYNC_WORK_NAME)
    }

    override fun cancelSubscribeToTags() {
        workManager.cancelUniqueWork(uniqueWorkName = SUBSCRIBE_TO_TAGS_WORK_NAME)
    }

    override fun cancelAnnouncementSync() {
        workManager.cancelUniqueWork(uniqueWorkName = ANNOUNCEMENT_SYNC_WORK_NAME)
    }

    override fun cancelAnnouncementAlerts() {
        workManager.cancelUniqueWork(uniqueWorkName = ANNOUNCEMENT_REFRESH_WORK_NAME)
    }
}

private fun Flow<List<WorkInfo>>.mapToSyncState(): Flow<SyncState> = map { workInfos ->
    val workInfo = workInfos.firstOrNull() ?: return@map SyncState.Idle

    when (workInfo.state) {
        WorkInfo.State.RUNNING -> SyncState.Syncing
        WorkInfo.State.ENQUEUED -> SyncState.Enqueued
        WorkInfo.State.BLOCKED -> SyncState.Blocked
        WorkInfo.State.SUCCEEDED -> SyncState.Success
        WorkInfo.State.FAILED -> SyncState.Error
        WorkInfo.State.CANCELLED -> SyncState.Idle
    }
}
