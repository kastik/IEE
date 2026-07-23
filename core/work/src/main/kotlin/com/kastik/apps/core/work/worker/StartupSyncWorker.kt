package com.kastik.apps.core.work.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.kastik.apps.core.crashlytics.Crashlytics
import com.kastik.apps.core.domain.usecases.SyncAnnouncementTagsUseCase
import com.kastik.apps.core.domain.usecases.SyncAuthorsUseCase
import com.kastik.apps.core.domain.usecases.SyncProfileUseCase
import com.kastik.apps.core.domain.usecases.SyncSubscribableTagsUseCase
import com.kastik.apps.core.domain.usecases.SyncSubscriptionsUseCase
import com.kastik.apps.core.model.error.NetworkError
import com.kastik.apps.core.notifications.NotificationIds.STARTUP_SYNC_NOTIFICATION_ID
import com.kastik.apps.core.notifications.Notifier
import com.kastik.apps.core.work.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.cancellation.CancellationException
import com.kastik.apps.core.model.result.Result as DomainResult

@HiltWorker
internal class StartupSyncWorker
@AssistedInject
constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier,
    private val syncAuthorsUseCase: SyncAuthorsUseCase,
    private val syncProfileUseCase: SyncProfileUseCase,
    private val syncSubscriptionsUseCase: SyncSubscriptionsUseCase,
    private val syncSubscribableTagsUseCase: SyncSubscribableTagsUseCase,
    private val syncAnnouncementTagsUseCase: SyncAnnouncementTagsUseCase,
    private val crashlytics: Crashlytics,
) : CoroutineWorker(context, workerParams) {

    // TODO We should update the announcement db too

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            STARTUP_SYNC_NOTIFICATION_ID,
            notifier.createSyncNotification(
                titleResId = R.string.sync_startup_title,
                bodyResId = R.string.sync_startup_body,
            ),
        )
    }

    override suspend fun doWork(): Result = coroutineScope {
        try {

            val results =
                listOf(
                        async { syncAuthorsUseCase() },
                        async { syncProfileUseCase() },
                        async { syncSubscriptionsUseCase() },
                        async { syncSubscribableTagsUseCase() },
                        async { syncAnnouncementTagsUseCase() },
                    )
                    .awaitAll()

            val hasRetriableError = results.any { result ->
                result is DomainResult.Error<*> && result.error != NetworkError.Authentication
            }

            if (hasRetriableError) {
                Result.retry()
            } else {
                Result.success()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Result.retry()
        }
    }
}
