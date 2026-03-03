package com.kastik.apps.core.work.scheduler

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.work.worker.AnnouncementAlertWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WorkSchedulerImpl @Inject constructor(
    @ApplicationContext context: Context
) : WorkScheduler {

    companion object {
        internal const val ANNOUNCEMENT_REFRESH_WORK_NAME = "ANNOUNCEMENT_REFRESH_WORK"
    }

    private val workManager = WorkManager.getInstance(context)


    override fun scheduleAnnouncementAlerts() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val announcementRefreshWorkRequest = PeriodicWorkRequestBuilder<AnnouncementAlertWorker>(
            30, TimeUnit.MINUTES
        ).setConstraints(constraints).setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = ANNOUNCEMENT_REFRESH_WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
            request = announcementRefreshWorkRequest
        )
    }

    override fun getAnnouncementAlertsWorkInfo(): Flow<WorkInfo?> =
        workManager.getWorkInfosForUniqueWorkFlow(ANNOUNCEMENT_REFRESH_WORK_NAME)
            .map { it.firstOrNull() }

    override fun cancelAnnouncementAlerts() {
        workManager.cancelUniqueWork(ANNOUNCEMENT_REFRESH_WORK_NAME)

    }
}