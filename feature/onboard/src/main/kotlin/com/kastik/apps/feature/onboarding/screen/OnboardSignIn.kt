package com.kastik.apps.feature.onboarding.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.feature.onboarding.R

@Composable
internal fun OnboardSignIn(
    isSignedIn: Boolean = false,
    onSignInClick: () -> Unit = {},
    onGuestClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
) {

    AnimatedContent(
        isSignedIn
    ) { state ->
        when (state) {
            true -> {
                OnboardSignInSignedIn(
                    onContinueClick = onContinueClick
                )
            }

            false -> {
                OnboardSignInPending(
                    onSignInClick = onSignInClick,
                    onExploreAsGuestClick = onGuestClick
                )
            }
        }
    }
}


@Composable
private fun OnboardSignInPending(
    onSignInClick: () -> Unit,
    onExploreAsGuestClick: () -> Unit,
) {
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularIcon(
                imageVector = Icons.Rounded.Lock,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Text(
                text = stringResource(R.string.signin_unauthenticated_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.signin_unauthenticated_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }



        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSignInClick()
                }
            ) {
                Text(stringResource(R.string.signin_unauthenticated_primary_action))
            }

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onExploreAsGuestClick()
                }
            ) { Text(stringResource(R.string.signin_unauthenticated_secondary_action)) }
        }
    }
}


@Composable
private fun OnboardSignInSignedIn(
    onContinueClick: () -> Unit,
) {
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularIcon(
                imageVector = Icons.Rounded.CheckCircle,
                containerColor = MaterialTheme.colorScheme.primary,
                tint = MaterialTheme.colorScheme.primaryContainer
            )
            Text(
                text = stringResource(R.string.signin_authenticated_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.signin_authenticated_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )


        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onContinueClick()
                }
            ) {
                Text(stringResource(R.string.signin_next_page))
            }
        }
    }
}

@Composable
fun CircularIcon(
    imageVector: ImageVector,
    containerColor: Color,
    tint: Color,
) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .background(containerColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = tint
        )
    }
}


@Preview
@Composable
private fun OnboardSignInSignedOutPreview() {
    IeePreview {
        OnboardSignIn(isSignedIn = false)
    }
}

@Preview
@Composable
private fun OnboardSignInSignedInPreview() {
    IeePreview {
        OnboardSignIn(isSignedIn = true)
    }
}