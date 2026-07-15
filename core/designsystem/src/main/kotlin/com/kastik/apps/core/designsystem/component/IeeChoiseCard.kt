package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Light
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//TODO Fix

@Composable
fun IeeChoiceCard(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconContentDescription: String? = null
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val border = if (isSelected) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        CardDefaults.outlinedCardBorder(enabled)
    }

    OutlinedCard(
        modifier = modifier
            .selectable(
                selected = isSelected,
                enabled = enabled,
                role = Role.RadioButton,
                onClick = onClick
            ),
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
        border = border
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon, contentDescription = iconContentDescription, tint = contentColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title, style = MaterialTheme.typography.labelLarge, color = contentColor
            )
        }
    }
}

@Preview
@Composable
private fun IeeChoiceCardSelectedPreview() {
    IeePreview {
        IeeChoiceCard(
            title = "The quick brown",
            icon = Icons.Default.Light,
            isSelected = true,
            onClick = {},
            enabled = true,
        )
    }

}

@Preview
@Composable
private fun IeeChoiceCardUnselectedPreview() {
    IeePreview {
        IeeChoiceCard(
            title = "The quick brown",
            icon = Icons.Default.Light,
            isSelected = false,
            onClick = {},
            enabled = true,
        )
    }

}