package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class TokenRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val authenticationRepository: AuthenticationRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            authenticationRepository.refreshAboardToken()
            return Result.success()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                authenticationRepository.clearAuthenticationData()
                return Result.failure()
            }
            return Result.retry()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}