package com.kastik.apps.core.data.repository

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.kastik.apps.core.common.di.IoDispatcher
import com.kastik.apps.core.domain.repository.NotificationRepository
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userPreferencesRepository: UserPreferencesRepository,
) : NotificationRepository {
    override fun areNotificationsEnabled(): Flow<Boolean> {

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return callbackFlow {
            trySend(nm.areNotificationsEnabled())

            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    trySend(nm.areNotificationsEnabled())
                }
            }

            val lifecycle = ProcessLifecycleOwner.get().lifecycle
            lifecycle.addObserver(observer)

            awaitClose { lifecycle.removeObserver(observer) }
        }.distinctUntilChanged()
    }

    override suspend fun toggleNotifications() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }
}