package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.usecases.CheckNewAnnouncementsUseCase
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.fold
import com.kastik.apps.core.notifications.Notifier
import com.kastik.apps.core.work.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
internal class AnnouncementAlertWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier,
    private val checkNewAnnouncementsUseCase: CheckNewAnnouncementsUseCase,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val newAnnouncements = checkNewAnnouncementsUseCase()
        return newAnnouncements.fold(
            onSuccess = { announcements ->
                announcements.forEach { announcement ->
                    notifier.sendAnnouncementNotification(
                        announcementId = announcement.id,
                        title = announcement.title,
                        body = announcement.preview
                    )
                }
                Result.success()
            },
            onError = { error ->
                if (error is NetworkError.Authentication) {
                    notifier.sendGeneralNotification(
                        titleResId = R.string.title_logged_out,
                        bodyResId = R.string.body_logged_out,
                        uri = R.string.url_logged_out
                    )
                    Result.failure()
                } else Result.retry()
            }
        )
    }
}