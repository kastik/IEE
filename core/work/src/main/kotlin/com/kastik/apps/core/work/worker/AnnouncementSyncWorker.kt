package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.usecases.SyncAnnouncementWithIdUseCase
import com.kastik.apps.core.model.result.fold
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
internal class AnnouncementSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncAnnouncementWithIdUseCase: SyncAnnouncementWithIdUseCase,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_ANNOUNCEMENT_ID = "announcement_id"
    }

    override suspend fun doWork(): Result {

        val announcementId = inputData.getInt(KEY_ANNOUNCEMENT_ID, 0)

        return syncAnnouncementWithIdUseCase(announcementId).fold(
            onSuccess = {
                Result.success()
            },
            onError = {
                if (runAttemptCount < 3) {
                    Result.retry()
                } else {
                    Result.failure()
                }
            }
        )
    }
}