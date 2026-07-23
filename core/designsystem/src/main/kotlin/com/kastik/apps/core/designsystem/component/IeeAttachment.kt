package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IeeAttachment(
    fileName: String,
    onClick: () -> Unit = {},
) {
    AssistChip(
        contentPadding =
            PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
        onClick = onClick,
        label = {
            Text(
                text = fileName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.AttachFile,
                contentDescription = fileName,
            )
        },
    )
}

@Preview
@Composable
private fun IeeAttachmentPreview() {
    IeePreview {
        IeeAttachment("Grades.pdf")
    }
}
