package com.kastik.apps

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.kastik.apps.core.common.di.ApplicationScope
import com.kastik.apps.core.dev.tools.DevTools
import com.kastik.apps.core.domain.usecases.FetchRemoteOptionsUseCase
import com.kastik.apps.core.domain.usecases.UpdateUserPropertiesUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class IEEApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var fetchRemoteOptionsUseCase: FetchRemoteOptionsUseCase

    @Inject
    @ApplicationScope
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var devTools: DevTools

    @Inject
    lateinit var updateUserPropertiesUseCase: UpdateUserPropertiesUseCase

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        devTools.setupStrictMode()
        devTools.setupLeakCanary()

        applicationScope.launch {
            fetchRemoteOptionsUseCase()
        }
        applicationScope.launch {
            updateUserPropertiesUseCase()
        }

    }

}