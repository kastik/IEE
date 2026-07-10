package com.kastik.apps.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.theme.IeeTheme
import com.kastik.apps.core.domain.service.WorkScheduler
import com.kastik.apps.core.model.user.Theme
import com.kastik.apps.core.ui.extensions.shouldUseDarkTheme
import com.kastik.apps.navigation.IeeNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IeeMainActivity : ComponentActivity() {
    @Inject
    lateinit var analytics: Analytics

    @Inject
    lateinit var workScheduler: WorkScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        workScheduler.scheduleStartupSync()

        setContent {
            val viewModel: IeeMainActivityViewModel = hiltViewModel()
            val appState by viewModel.appState.collectAsStateWithLifecycle()
            IeeTheme(
                darkTheme = appState.theme.shouldUseDarkTheme(),
                dynamicColor = appState.dynamicColor
            ) {
                CompositionLocalProvider(LocalAnalytics provides analytics) {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.semantics {
                            testTagsAsResourceId = true
                        }
                    ) {
                        IeeNavHost()
                    }
                }
            }
        }
    }
}

data class IeeAppState(
    val theme: Theme,
    val dynamicColor: Boolean
)