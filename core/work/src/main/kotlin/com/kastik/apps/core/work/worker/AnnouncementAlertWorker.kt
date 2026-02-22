package com.kastik.apps.core.work.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.repository.RemoteConfigRepository
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.CheckNewAnnouncementsUseCase
import com.kastik.apps.core.model.error.AuthenticationError
import com.kastik.apps.core.model.result.Result.Error
import com.kastik.apps.core.model.result.Result.Success
import com.kastik.apps.core.work.scheduler.WorkSchedulerImpl.Companion.ANNOUNCEMENT_REFRESH_WORK_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class AnnouncementAlertWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier,
    private val checkNewAnnouncementsUseCase: CheckNewAnnouncementsUseCase,
    private val remoteConfigRepository: RemoteConfigRepository
) : CoroutineWorker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {

        if (remoteConfigRepository.isFcmEnabled()) {
            WorkManager.getInstance(applicationContext)
                .cancelUniqueWork(ANNOUNCEMENT_REFRESH_WORK_NAME)
            return Result.success()
        }

        when (val newAnnouncements = checkNewAnnouncementsUseCase()) {
            is Success -> {
                newAnnouncements.data.forEach { announcement ->
                    notifier.sendPushNotification(
                        announcementId = announcement.id,
                        title = announcement.title,
                        body = announcement.preview
                    )
                }
                return Result.success()
            }

            is Error -> {
                if (newAnnouncements.error is AuthenticationError) {
                    notifier.sendPushNotification(
                        title = "You have been logged out.",
                        body = "Please sign in again to continue receiving notifications."
                    )
                    return Result.failure()
                }
                return Result.retry()
            }
        }
    }
}