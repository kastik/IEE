package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProfileSubscribedTags(
    subscribedTagTitles: List<String>,
    showTagSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.NotificationsActive, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Subscribed Tags", style = MaterialTheme.typography.titleMedium)
                }
                IconButton(
                    onClick = { showTagSheet(true) }
                ) {
                    Icon(Icons.Outlined.Settings, null)
                }
            }

            if (subscribedTagTitles.isEmpty()) {
                Text(
                    "You haven’t subscribed to any tags yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subscribedTagTitles.forEachIndexed { index, tag ->
                        FunkyChip(
                            tag
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfileSubscribedTagsPreview() {
    ProfileSubscribedTags(
        subscribedTagTitles = listOf("1st semester", "2nd semester", "General"),
        showTagSheet = {},
    )
}