package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.usecases.RefreshTokenUseCase
import com.kastik.apps.core.model.error.AuthenticationError
import com.kastik.apps.core.model.result.Result
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val result = refreshTokenUseCase()

        if (result is Result.Error && result.error is AuthenticationError) {
            return Result.failure()
        } else {
            Result.retry()
        }
        return Result.success()
    }
}