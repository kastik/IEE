package com.kastik.apps.core.work.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.CheckNewAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.StoreNotifiedAnnouncementIdsUseCase
import com.kastik.apps.core.model.result.Result.Error
import com.kastik.apps.core.model.result.Result.Success
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class AnnouncementAlertWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier,
    private val checkNewAnnouncementsUseCase: CheckNewAnnouncementsUseCase,
    private val storeNotifiedAnnouncementIdsUseCase: StoreNotifiedAnnouncementIdsUseCase,
) : CoroutineWorker(context, workerParams) {

    //TODO Store and check last sync time
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        when (val newAnnouncements = checkNewAnnouncementsUseCase()) {
            is Success -> {
                newAnnouncements.data.forEach { announcement ->
                    notifier.sendPushNotification(
                        announcementId = announcement.id,
                        title = announcement.title,
                        body = announcement.preview
                    )
                    storeNotifiedAnnouncementIdsUseCase(announcement.id)
                }
                return Result.success()
            }

            is Error -> {
                if (runAttemptCount > 10) {
                    return Result.failure()
                }
                return Result.retry()
            }
        }
    }
}