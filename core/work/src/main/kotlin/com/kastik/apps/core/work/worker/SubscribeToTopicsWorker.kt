package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.usecases.SyncTopicSubscriptionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import com.kastik.apps.core.model.result.Result as ResultDomain

@HiltWorker
class SubscribeToTopicsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncTopicSubscriptionsUseCase: SyncTopicSubscriptionsUseCase,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = try {
        withContext(Dispatchers.IO + NonCancellable) {
            val result = syncTopicSubscriptionsUseCase()
            if (result is ResultDomain.Success) {
                Result.success()
            } else {
                Result.retry()
            }
        }
    } catch (e: Exception) {
        Result.retry()
    }

}