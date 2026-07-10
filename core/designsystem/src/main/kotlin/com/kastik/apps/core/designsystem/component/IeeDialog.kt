package com.kastik.apps.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun IeeDialog(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector? = null,
    text: String,
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        icon = {
            if (icon != null) {
                Icon(icon, contentDescription = null)
            }
        },
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        })
}

@Preview(showBackground = true)
@Composable
private fun PreviewIeeDialog() {
    IeePreview {
        IeeDialog(
            icon = Icons.Default.NotificationsActive,
            title = "Stay updated",
            text = "Turn on notifications to never miss an important announcement.",
            confirmText = "Allow",
            onConfirm = {},
            dismissText = "Dismiss",
            onDismiss = {}
        )
    }
}