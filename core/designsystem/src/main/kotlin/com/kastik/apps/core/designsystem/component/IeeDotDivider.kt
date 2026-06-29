package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IeeDotDivider() {
    Box(
        modifier = Modifier
            .size(4.dp)
            .background(
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f), shape = CircleShape
            )
    )
}

@Preview
@Composable
fun IeeDotDividerPreview() {
    IeePreview {
        IeeDotDivider()
    }
}