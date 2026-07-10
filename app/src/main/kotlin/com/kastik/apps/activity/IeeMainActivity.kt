package com.kastik.apps.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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

    private val viewModel: IeeMainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.appState.value is IeeAppState.Loading
        }

        workScheduler.scheduleStartupSync()

        setContent {
            val appState by viewModel.appState.collectAsStateWithLifecycle()

            when (val state = appState) {
                IeeAppState.Loading -> Unit

                is IeeAppState.Loaded -> {
                    IeeTheme(
                        darkTheme = state.theme.shouldUseDarkTheme(),
                        dynamicColor = state.dynamicColor
                    ) {
                        CompositionLocalProvider(LocalAnalytics provides analytics) {
                            Surface(
                                color = MaterialTheme.colorScheme.background,
                                modifier = Modifier.semantics {
                                    testTagsAsResourceId = true
                                }
                            ) {
                                IeeNavHost(
                                    hasFinishedOnboarding = state.hasFinishedOnboarding
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed interface IeeAppState {

    data object Loading : IeeAppState

    data class Loaded(
        val theme: Theme,
        val dynamicColor: Boolean,
        val hasFinishedOnboarding: Boolean
    ) : IeeAppState
}

