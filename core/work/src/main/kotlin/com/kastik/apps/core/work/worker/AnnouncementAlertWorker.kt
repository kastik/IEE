package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.domain.usecases.CheckNewAnnouncementsUseCase
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.model.result.Result.Error
import com.kastik.apps.core.model.result.Result.Success
import com.kastik.apps.core.work.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException


@HiltWorker
class AnnouncementAlertWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier,
    private val crashlytics: Crashlytics,
    private val checkNewAnnouncementsUseCase: CheckNewAnnouncementsUseCase,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = try {

        when (val newAnnouncements = checkNewAnnouncementsUseCase()) {
            is Success -> {
                newAnnouncements.data.forEach { announcement ->
                    notifier.sendAnnouncementNotification(
                        announcementId = announcement.id,
                        title = announcement.title,
                        body = announcement.preview
                    )
                }
                return Result.success()
            }

            is Error -> {
                if (newAnnouncements.error is NetworkError.Authentication) {
                    notifier.sendGeneralNotification(
                        titleResId = R.string.title_logged_out,
                        bodyResId = R.string.body_logged_out,
                        uri = R.string.url_logged_out
                    )
                    return Result.failure()
                }
                return Result.retry()
            }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        crashlytics.recordException(e)
        return Result.retry()
    }
}