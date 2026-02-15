package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.usecases.RefreshEmailSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.SubscribeToEmailTagsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import com.kastik.apps.core.model.result.Result as ResultDomain

@HiltWorker
class SubscribeToTagsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val subscribeToEmailTagsUseCase: SubscribeToEmailTagsUseCase,
    private val refreshEmailSubscriptionsUseCase: RefreshEmailSubscriptionsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val tagIds = inputData.getIntArray("KEY_TAG_IDS")?.toList()
            ?: return Result.failure()

        return try {
            withContext(Dispatchers.IO + NonCancellable) {
                val subscribeResult = subscribeToEmailTagsUseCase(tagIds)
                val refreshResult = refreshEmailSubscriptionsUseCase()
                if (subscribeResult is ResultDomain.Success && refreshResult is ResultDomain.Success) {
                    Result.success()
                } else {
                    Result.retry()
                }
            }

        } catch (e: Exception) {
            Result.retry()
        }
    }
}