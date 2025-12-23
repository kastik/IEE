package com.kastik.apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.user.UserTheme
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.shouldUseDarkTheme
import com.kastik.apps.navigation.IEENavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val appState by viewModel.appState.collectAsStateWithLifecycle()
            AppsAboardTheme(
                darkTheme = appState.theme.shouldUseDarkTheme(),
                dynamicColor = appState.dynamicColor
            ) {
                CompositionLocalProvider(LocalAnalytics provides analytics) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        IEENavHost()
                    }
                }
            }
        }
    }
}

data class IEEAppState(
    val theme: UserTheme,
    val dynamicColor: Boolean
)