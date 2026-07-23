package com.kastik.apps.core.designsystem.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.kastik.apps.core.analytics.NoOpAnalytics
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.theme.IeeTheme

@Composable
fun IeePreview(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAnalytics provides NoOpAnalytics) {
        IeeTheme {
            Surface {
                content()
            }
        }
    }
}
