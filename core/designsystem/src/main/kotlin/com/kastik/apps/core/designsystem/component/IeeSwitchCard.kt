package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun IeeSwitchCard(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    enabled: Boolean = true,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.5f),
        colors =
            CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        IeeSwitchRow(
            title = title,
            subtitle = subtitle,
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Preview
@Composable
private fun IeeSwitchCardPreview() {
    IeePreview {
        IeeSwitchCard(
            title = "The quick brown fox",
            checked = true,
            onCheckedChange = {},
            subtitle = "Description",
            enabled = true,
        )
    }
}
