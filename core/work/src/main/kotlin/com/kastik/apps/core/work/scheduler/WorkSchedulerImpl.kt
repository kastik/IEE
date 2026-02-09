package com.kastik.apps.core.work.scheduler

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.work.worker.AnnouncementAlertWorker
import com.kastik.apps.core.work.worker.TokenRefreshWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WorkSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WorkScheduler {

    companion object {
        const val TOKEN_REFRESH_WORK_NAME = "TOKEN_REFRESH_WORK"
        const val ANNOUNCEMENT_REFRESH_WORK_NAME = "ANNOUNCEMENT_REFRESH_WORK"
    }

    override fun scheduleTokenRefresh() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val tokenRefreshWorkRequest =
            PeriodicWorkRequestBuilder<TokenRefreshWorker>(8, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = TOKEN_REFRESH_WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
            request = tokenRefreshWorkRequest
        )
    }


    override fun scheduleAnnouncementAlerts() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val announcementRefreshWorkRequest =
            PeriodicWorkRequestBuilder<AnnouncementAlertWorker>(30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = ANNOUNCEMENT_REFRESH_WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
            request = announcementRefreshWorkRequest
        )
    }

    override fun cancelTokenRefresh() {
        WorkManager.getInstance(context).cancelUniqueWork(TOKEN_REFRESH_WORK_NAME)
    }

    override fun cancelAnnouncementAlerts() {
        WorkManager.getInstance(context).cancelUniqueWork(ANNOUNCEMENT_REFRESH_WORK_NAME)
    }

}