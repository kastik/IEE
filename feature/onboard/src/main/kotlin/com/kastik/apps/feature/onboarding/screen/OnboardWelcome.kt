package com.kastik.apps.feature.onboarding.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WavingHand
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.feature.onboarding.OnboardingHeroScreen

@Composable
internal fun OnboardWelcome(
    onGetStartedClick: () -> Unit = {}
) {
    OnboardingHeroScreen(
        icon = Icons.Rounded.WavingHand,
        iconTint = MaterialTheme.colorScheme.onSecondaryContainer,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        title = "Welcome!",
        description = "Your personalized hub for announcements and updates. Let's get things set up.",
        primaryButtonText = "Get Started",
        onPrimaryClick = onGetStartedClick
    )
}

@Preview
@Composable
fun OnboardWelcomePreview() {
    IeePreview {
        OnboardWelcome()
    }
}