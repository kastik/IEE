package com.kastik.apps.feature.onboarding.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.feature.onboarding.OnboardingHeroScreen

@Composable
internal fun OnboardNotifications(
    areNotificationsAllowed: Boolean = false,
    onAllowClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
) {
    AnimatedContent(
        targetState = areNotificationsAllowed, contentKey = { it }

    ) { state ->
        when (state) {
            true -> {
                OnboardingHeroScreen(
                    icon = Icons.Rounded.NotificationsActive,
                    iconTint = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    title = "You're all set",
                    description = "Notifications are enabled. You'll receive important updates and urgent alerts directly to your device.",
                    primaryButtonText = "Continue",
                    onPrimaryClick = onContinueClick
                )
            }

            false -> {
                OnboardingHeroScreen(
                    icon = Icons.Rounded.NotificationsActive,
                    iconTint = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    title = "Stay in the loop",
                    description = "Turn on notifications so we can keep you updated with urgent alerts, new grades, and important department news.",
                    primaryButtonText = "Allow Notifications",
                    onPrimaryClick = onAllowClick,
                    secondaryButtonText = "Not Now",
                    onSecondaryClick = onSkipClick
                )
            }
        }
    }
}


@Preview
@Composable
fun OnboardNotificationsNotGrantedPreview() {
    IeePreview {
        OnboardNotifications(false)
    }
}

@Preview
@Composable
fun OnboardNotificationsGrantedPreview() {
    IeePreview {
        OnboardNotifications(areNotificationsAllowed = true)
    }
}