package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IeeStatusBanner(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    actionTextColor: Color = MaterialTheme.colorScheme.primary,
) {
    ElevatedCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                    )
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (!actionLabel.isNullOrEmpty() && onActionClick != null) {
                FilledTonalButton(onClick = onActionClick) {
                    Text(
                        text = actionLabel,
                        color = actionTextColor,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun IeeStatusBannerWithoutActionAndIconPreview() {
    IeePreview {
        IeeStatusBanner(
            text = "You are offline",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun IeeStatusBannerWithoutActionPreview() {
    IeePreview {
        IeeStatusBanner(
            text = "You are offline",
            icon = Icons.Default.CloudOff,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun IeeStatusBannerWithActionPreview() {
    IeePreview {
        IeeStatusBanner(
            text = "You are offline",
            icon = Icons.Default.CloudOff,
            actionLabel = "Retry",
            onActionClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
