package com.kastik.apps.feature.onboarding.screen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.SettingsBrightness
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeeChoiceCard
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeSwitchCard
import com.kastik.apps.core.model.user.Theme
import com.kastik.feature.onboarding.R

@Composable
internal fun OnboardAppearance(
    selectedTheme: Theme = Theme.FOLLOW_SYSTEM,
    dynamicColorEnabled: Boolean = false,
    onThemeSelected: (Theme) -> Unit = {},
    onDynamicColorToggled: (Boolean) -> Unit = {},
    onContinueClick: () -> Unit = {},
) {
    val haptics = LocalHapticFeedback.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.appearance_theme_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.appearance_theme_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IeeChoiceCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.appearance_theme_system),
                    icon = Icons.Rounded.SettingsBrightness,
                    isSelected = selectedTheme == Theme.FOLLOW_SYSTEM,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onThemeSelected(Theme.FOLLOW_SYSTEM)
                    })
                IeeChoiceCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.appearance_theme_light),
                    icon = Icons.Rounded.LightMode,
                    isSelected = selectedTheme == Theme.LIGHT,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onThemeSelected(Theme.LIGHT)
                    })
                IeeChoiceCard(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.appearance_theme_dark),
                    icon = Icons.Rounded.DarkMode,
                    isSelected = selectedTheme == Theme.DARK,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onThemeSelected(Theme.DARK)
                    })
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                IeeSwitchCard(
                    title = stringResource(R.string.appearance_dynamic_color_title),
                    subtitle = stringResource(R.string.appearance_dynamic_color_body),
                    checked = dynamicColorEnabled,
                    onCheckedChange = {
                        onDynamicColorToggled(it)
                    })
            }
        }

        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onContinueClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) { Text(stringResource(R.string.appearance_next_page)) }
    }
}


@Preview
@Composable
private fun OnboardAppearancePreview() {
    IeePreview {
        OnboardAppearance()
    }
}