package com.kastik.apps.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme

@Composable
fun SelectableTagItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val shape = RoundedCornerShape(20)
    val containerColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surface
    )
    val contentColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
        else MaterialTheme.colorScheme.onSurface
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clip(shape)
            .clickable(onClick = onToggle),
        color = containerColor,
        tonalElevation = if (isSelected) 3.dp else 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
                modifier = Modifier.weight(1f)
            )

            AnimatedVisibility(visible = isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Preview(name = "Selected Tag Item")
@Composable
private fun SelectableTagItemSelectedPreview() {
    AppsAboardTheme {
        SelectableTagItem(
            title = "Selected tag",
            isSelected = true,
            onToggle = {}
        )
    }
}

@Preview(name = "Unselected Tag Item")
@Composable
private fun SelectableTagItemUnselectedPreview() {
    AppsAboardTheme {
        SelectableTagItem(
            title = "Not selected tag",
            isSelected = false,
            onToggle = {}
        )
    }
}
