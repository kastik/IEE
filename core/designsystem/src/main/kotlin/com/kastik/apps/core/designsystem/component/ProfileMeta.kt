package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileMeta(
    modifier: Modifier = Modifier,
    isAdmin: Boolean,
    isAuthor: Boolean,
    lastLogin: String,
    createdAt: String,
) {
    ElevatedCard(
        shape = RoundedCornerShape(22.dp), modifier = modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.ManageAccounts, null)
                Spacer(Modifier.width(8.dp))
                Text("Account Info", style = MaterialTheme.typography.titleMedium)
            }

            MetaText(
                "Role", when {
                    isAdmin -> "Administrator"
                    isAuthor -> "Author"
                    else -> "Student"
                }
            )

            MetaText("Last Login", lastLogin)
            MetaText("Joined", createdAt)
        }
    }
}

@Composable
private fun MetaText(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview
@Composable
fun ProfileMetaPreview() {
    ProfileMeta(
        isAdmin = false,
        isAuthor = false,
        lastLogin = "25-10-2025 12:35",
        createdAt = "25-10-2025 12:10"
    )
}
