package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.usecases.SyncAnnouncementWithIdUseCase
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.fold
import com.kastik.apps.core.notifications.NotificationIds.ANNOUNCEMENT_SYNC_NOTIFICATION_ID
import com.kastik.apps.core.notifications.Notifier
import com.kastik.apps.core.work.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class AnnouncementSyncWorker
@AssistedInject
constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier,
    private val syncAnnouncementWithIdUseCase: SyncAnnouncementWithIdUseCase,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_ANNOUNCEMENT_ID = "announcement_id"
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            ANNOUNCEMENT_SYNC_NOTIFICATION_ID,
            notifier.createSyncNotification(
                titleResId = R.string.sync_announcement_title,
                bodyResId = R.string.sync_announcement_body,
            ),
        )
    }

    override suspend fun doWork(): Result {

        val announcementId = inputData.getInt(KEY_ANNOUNCEMENT_ID, 0)

        return syncAnnouncementWithIdUseCase(announcementId)
            .fold(
                onSuccess = {
                    Result.success()
                },
                onError = { error ->
                    if (runAttemptCount < 3 && error !is NetworkError.Authentication) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                },
            )
    }
}
