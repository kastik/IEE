package com.kastik.apps.core.designsystem.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.kastik.apps.core.analytics.Analytics
import com.kastik.apps.core.analytics.AnalyticsEvent
import com.kastik.apps.core.analytics.AnalyticsEventTypes
import com.kastik.apps.core.analytics.AnalyticsParamKeys
import com.kastik.apps.core.analytics.EmptyAnalyticsEventTypes
import com.kastik.apps.core.analytics.EmptyAnalyticsParamKeys
import com.kastik.apps.core.designsystem.extensions.LocalAnalytics
import com.kastik.apps.core.designsystem.theme.IeeTheme

@Composable
fun IeePreview(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAnalytics provides NoOpAnalytics
    ) {
        IeeTheme {
            Surface {
                content()
            }
        }
    }
}

internal object NoOpAnalytics : Analytics {
    override val Types: AnalyticsEventTypes = EmptyAnalyticsEventTypes
    override val ParamKeys: AnalyticsParamKeys = EmptyAnalyticsParamKeys
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun setUserId(userId: String?) = Unit
    override fun setUserProperty(propertyName: String, value: String?) = Unit
}