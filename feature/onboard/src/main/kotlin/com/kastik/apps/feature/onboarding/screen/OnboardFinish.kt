package com.kastik.apps.feature.onboarding.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.feature.onboarding.OnboardingHeroScreen

@Composable
internal fun OnboardFinish(onFinish: () -> Unit = {}) {
    OnboardingHeroScreen(
        icon = Icons.Rounded.Done,
        iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        title = "All Set!",
        description = "You're all setup and ready to go.",
        primaryButtonText = "Finish",
        onPrimaryClick = onFinish
    )
}

@Preview
@Composable
fun OnboardFinishPreview() {
    IeePreview {
        OnboardFinish()
    }
}