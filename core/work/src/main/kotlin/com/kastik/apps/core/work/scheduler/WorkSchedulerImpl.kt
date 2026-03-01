package com.kastik.apps.core.work.scheduler

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.work.worker.AnnouncementAlertWorker
import com.kastik.apps.core.work.worker.SubscribeToTagsWorker
import com.kastik.apps.core.work.worker.SubscribeToTopicsWorker
import com.kastik.apps.core.work.worker.TokenRefreshWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WorkSchedulerImpl @Inject constructor(
    @ApplicationContext context: Context
) : WorkScheduler {

    companion object {
        internal const val TOKEN_REFRESH_WORK_NAME = "TOKEN_REFRESH_WORK"
        internal const val SYNC_TOPICS_WORK_NAME = "SYNC_TOPICS_WORK"
        internal const val SUBSCRIBE_TO_TAGS_WORK_NAME = "SUBSCRIBE_TO_TAGS_WORK"
        internal const val ANNOUNCEMENT_REFRESH_WORK_NAME = "ANNOUNCEMENT_REFRESH_WORK"
    }

    private val workManager = WorkManager.getInstance(context)

    //TODO When aboard refresh logic is merged this will be redundant
    override fun scheduleTokenRefresh() {

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val tokenRefreshWorkRequest =
            PeriodicWorkRequestBuilder<TokenRefreshWorker>(8, TimeUnit.HOURS).setConstraints(
                constraints
            ).setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES).build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = TOKEN_REFRESH_WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
            request = tokenRefreshWorkRequest
        )
    }

    override fun getTokenRefreshWorkInfo(): Flow<WorkInfo?> =
        workManager.getWorkInfosForUniqueWorkFlow(TOKEN_REFRESH_WORK_NAME).map { it.firstOrNull() }

    override fun cancelTokenRefresh() {
        workManager.cancelUniqueWork(TOKEN_REFRESH_WORK_NAME)

    }

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

    override fun scheduleSubscribeToTags(newTagIds: List<Int>) {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val inputData = workDataOf("KEY_TAG_IDS" to newTagIds.toIntArray())

        val subscribeToTagsWork =
            OneTimeWorkRequestBuilder<SubscribeToTagsWorker>().setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES).build()

        val subscribeToTopicWork =
            OneTimeWorkRequestBuilder<SubscribeToTopicsWorker>().setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES).build()

        workManager.beginUniqueWork(
            SUBSCRIBE_TO_TAGS_WORK_NAME, ExistingWorkPolicy.REPLACE, subscribeToTagsWork
        ).then(subscribeToTopicWork).enqueue()

    }

    override fun getSubscribeToTagsWorkInfo(): Flow<WorkInfo?> =
        workManager.getWorkInfosForUniqueWorkFlow(SUBSCRIBE_TO_TAGS_WORK_NAME)
            .map { it.firstOrNull() }

    override fun cancelSubscribeToTags() {
        workManager.cancelUniqueWork(SUBSCRIBE_TO_TAGS_WORK_NAME)

    }

    override fun scheduleTopicsSync() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val subscribeToTopicWork =
            OneTimeWorkRequestBuilder<SubscribeToTopicsWorker>().setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES).build()

        workManager.beginUniqueWork(
            SYNC_TOPICS_WORK_NAME, ExistingWorkPolicy.REPLACE, subscribeToTopicWork
        ).enqueue()
    }

    override fun getTopicsSyncWorkInfo(): Flow<WorkInfo?> =
        workManager.getWorkInfosForUniqueWorkFlow(SYNC_TOPICS_WORK_NAME)
            .map { it.firstOrNull() }

    override fun cancelTopicsSync() {
        workManager.cancelUniqueWork(SYNC_TOPICS_WORK_NAME)

    }
}