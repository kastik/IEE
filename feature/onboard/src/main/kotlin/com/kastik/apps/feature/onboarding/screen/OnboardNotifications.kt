package com.kastik.apps.feature.onboarding.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.component.IeePreview
import com.kastik.feature.onboarding.R

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
                OnboardNotificationsGranted(
                    onContinueClick = onContinueClick
                )
            }

            false -> {
                OnboardNotificationsNotGranted(
                    onAllowClick = onAllowClick,
                    onNotNowClick = onSkipClick,
                )
            }
        }
    }
}


@Composable
private fun OnboardNotificationsGranted(
    onContinueClick: () -> Unit
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
                imageVector = Icons.Rounded.NotificationsActive,
                containerColor = MaterialTheme.colorScheme.primary,
                tint = MaterialTheme.colorScheme.primaryContainer
            )
            Text(
                text = stringResource(R.string.notifications_permission_accepted_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.notifications_permission_accepted_body),
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
                Text(stringResource(R.string.notifications_next_page))
            }
        }
    }
}

@Composable
private fun OnboardNotificationsNotGranted(
    onAllowClick: () -> Unit,
    onNotNowClick: () -> Unit,
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
                imageVector = Icons.Rounded.NotificationsActive,
                containerColor = MaterialTheme.colorScheme.primary,
                tint = MaterialTheme.colorScheme.primaryContainer
            )
            Text(
                text = stringResource(R.string.notifications_permission_rejected_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.notifications_permission_rejected_body),
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
                    onAllowClick()
                }
            ) {
                Text(stringResource(R.string.notification_rejected_primary_action))
            }
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onNotNowClick()
                }
            ) {
                Text(stringResource(R.string.notification_rejected_secondary_action))
            }
        }
    }


}


@Preview
@Composable
private fun OnboardNotificationsNotGrantedPreview() {
    IeePreview {
        OnboardNotifications(false)
    }
}

@Preview
@Composable
private fun OnboardNotificationsGrantedPreview() {
    IeePreview {
        OnboardNotifications(areNotificationsAllowed = true)
    }
}