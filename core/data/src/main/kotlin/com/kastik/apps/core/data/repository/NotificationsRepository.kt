package com.kastik.apps.core.data.repository

import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.kastik.apps.core.domain.repository.NotificationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf

@Singleton
internal class NotificationsRepositoryImpl
@Inject
constructor(@ApplicationContext private val context: Context) : NotificationRepository {
    override fun areNotificationsEnabled(): Flow<Boolean> {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return flowOf(true)
        }

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val application = context.applicationContext as Application

        return callbackFlow {
                trySend(nm.areNotificationsEnabled())

                val lifecycleCallbacks =
                    object : Application.ActivityLifecycleCallbacks {
                        override fun onActivityResumed(activity: Activity) {
                            trySend(nm.areNotificationsEnabled())
                        }

                        override fun onActivityCreated(
                            activity: Activity,
                            savedInstanceState: Bundle?,
                        ) {}

                        override fun onActivityStarted(activity: Activity) {}

                        override fun onActivityPaused(activity: Activity) {}

                        override fun onActivityStopped(activity: Activity) {}

                        override fun onActivitySaveInstanceState(
                            activity: Activity,
                            outState: Bundle,
                        ) {}

                        override fun onActivityDestroyed(activity: Activity) {}
                    }

                application.registerActivityLifecycleCallbacks(lifecycleCallbacks)

                awaitClose {
                    application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
                }
            }
            .distinctUntilChanged()
    }
}
