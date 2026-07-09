package com.kastik.apps.feature.onboarding.screen

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeeChoiceCard
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.core.designsystem.component.IeeSwitchCard
import com.kastik.apps.core.model.user.Theme

@Composable
internal fun OnboardAppearance(
    selectedTheme: Theme = Theme.FOLLOW_SYSTEM,
    dynamicColorEnabled: Boolean = false,
    onThemeSelected: (Theme) -> Unit = {},
    onDynamicColorToggled: (Boolean) -> Unit = {},
    onContinueClick: () -> Unit = {},
) {
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose your look",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        Text(
            text = "You can always change this later in settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IeeChoiceCard(
                modifier = Modifier.weight(1f),
                title = "System",
                icon = Icons.Rounded.SettingsBrightness,
                isSelected = selectedTheme == Theme.FOLLOW_SYSTEM,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onThemeSelected(Theme.FOLLOW_SYSTEM)
                })
            IeeChoiceCard(
                modifier = Modifier.weight(1f),
                title = "Light",
                icon = Icons.Rounded.LightMode,
                isSelected = selectedTheme == Theme.LIGHT,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onThemeSelected(Theme.LIGHT)
                })
            IeeChoiceCard(
                modifier = Modifier.weight(1f),
                title = "Dark",
                icon = Icons.Rounded.DarkMode,
                isSelected = selectedTheme == Theme.DARK,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onThemeSelected(Theme.DARK)
                })
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            IeeSwitchCard(
                title = "Dynamic Color",
                subtitle = "Extracts accent colors from your system wallpaper for a personalized feel.",
                checked = dynamicColorEnabled,
                onCheckedChange = {
                    onDynamicColorToggled(it)
                })
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onContinueClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) { Text("Continue") }
    }
}


@Preview
@Composable
fun OnboardAppearancePreview() {
    IeePreview {
        OnboardAppearance()
    }
}