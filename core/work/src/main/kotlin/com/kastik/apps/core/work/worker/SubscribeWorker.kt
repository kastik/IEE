package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import com.kastik.apps.core.model.result.fold
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class SubscribeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_TAG_IDS = "tag_ids"
    }

    override suspend fun doWork(): Result {

        val tagIdsArray = inputData.getIntArray(KEY_TAG_IDS)
        val tagIdsList = tagIdsArray?.toList() ?: emptyList()

        return subscribeToTagsUseCase(tagIdsList).fold(
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
