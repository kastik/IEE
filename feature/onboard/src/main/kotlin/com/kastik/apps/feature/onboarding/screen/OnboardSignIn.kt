package com.kastik.apps.feature.onboarding.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Login
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.apps.feature.onboarding.OnboardingHeroScreen

@Composable
internal fun OnboardSignIn(
    isSignedIn: Boolean = false,
    onSignInClick: () -> Unit = {},
    onGuestClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
) {
    if (isSignedIn) {
        OnboardingHeroScreen(
            icon = Icons.Rounded.CheckCircle,
            iconTint = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            title = "Authenticated",
            description = "Your IEE account is securely linked. You're ready to proceed.",
            primaryButtonText = "Continue",
            onPrimaryClick = onContinueClick
        )
    } else {
        OnboardingHeroScreen(
            icon = Icons.Rounded.Lock,
            iconTint = MaterialTheme.colorScheme.onTertiaryContainer,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            title = "Authenticate",
            description = "Sign in with your IEE account to access your personalized dashboard, grades, and private announcements.",
            primaryButtonText = "Sign In with IEE",
            primaryButtonIcon = Icons.AutoMirrored.Rounded.Login,
            onPrimaryClick = onSignInClick,
            secondaryButtonText = "Explore as Guest",
            onSecondaryClick = onGuestClick
        )
    }
}


@Preview
@Composable
fun OnboardSignInSignedOutPreview() {
    IeePreview {
        OnboardSignIn(isSignedIn = false)
    }
}

@Preview
@Composable
fun OnboardSignInSignedInPreview() {
    IeePreview {
        OnboardSignIn(isSignedIn = true)
    }
}

