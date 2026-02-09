package com.kastik.apps.core.work.worker

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.PreferencesLocalDataSource
import com.kastik.apps.core.datastore.ProfileLocalDataSource
import com.kastik.apps.core.domain.service.Notifier
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.network.datasource.AnnouncementRemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException


@HiltWorker
class AnnouncementAlertWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notifier: Notifier,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val userPreferences: PreferencesLocalDataSource,
    private val authenticationRepository: AuthenticationLocalDataSource,
) : CoroutineWorker(context, workerParams) {

    private companion object {
        const val PAGE_SIZE = 20
        const val MAX_PAGES_SAFETY_LIMIT = 2
        const val MAX_HISTORY_SIZE = 50
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        return try {
            val storedIds = userPreferences.getNotifiedAnnouncementIds().first().toMutableSet()

            val profile = profileLocalDataSource.getProfile().firstOrNull()
                ?: return Result.failure()

            val subscribedTagIds = profile.subscribedTags.subscribedTagsList
                .map { it.id }
                .toSet()

            if (subscribedTagIds.isEmpty()) return Result.success()

            var page = 1

            while (page <= MAX_PAGES_SAFETY_LIMIT) {
                val response = announcementRemoteDataSource.fetchPagedAnnouncements(
                    page = page,
                    perPage = PAGE_SIZE,
                    sortBy = SortType.DESC,
                    tagId = subscribedTagIds.toList()
                )

                val announcements = response.data
                if (announcements.isEmpty()) break

                val newAnnouncements = announcements.filter { !storedIds.contains(it.id) }

                if (newAnnouncements.isNotEmpty()) {
                    newAnnouncements.forEach { announcement ->
                        notifier.sendPushNotification(
                            announcementId = announcement.id,
                            title = announcement.title,
                            body = announcement.preview
                        )

                        storedIds.add(announcement.id)
                    }
                }
                page++
            }

            val updatedList = storedIds.toList().takeLast(MAX_HISTORY_SIZE)
            userPreferences.setNotifiedAnnouncementIds(updatedList)

            Result.success()

        } catch (e: HttpException) {
            if (e.code() == 401) {
                Result.failure()
            } else if (e.code() in 500..599) {
                Result.retry()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}