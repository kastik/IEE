package com.kastik.apps.core.designsystem.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingToolBar(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    expandedAction: () -> Unit,
    collapsedAction: () -> Unit,
    expandedIcon: @Composable () -> Unit,
    collapsedIcon: @Composable () -> Unit,
) {
    HorizontalFloatingToolbar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        expanded = expanded,
        contentPadding = PaddingValues(0.dp),
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = if (expanded) collapsedAction else expandedAction
            ) {
                Crossfade(targetState = expanded, label = "fab-icon") { isExpanded ->
                    if (isExpanded) collapsedIcon() else expandedIcon()
                }
            }
        }
    ) {

    }
}

@Preview
@Composable
fun FloatingToolBarExpandedPreview() {
    MaterialTheme {
        FloatingToolBar(
            expanded = false,
            expandedAction = {},
            collapsedAction = { },
            expandedIcon = { Icon(Icons.Default.ArrowUpward, null) },
            collapsedIcon = { Icon(Icons.Default.Search, null) },
        )
    }
}

@Preview
@Composable
fun FloatingToolBarCollapsedPreview() {
    MaterialTheme {
        FloatingToolBar(
            expanded = true,
            expandedAction = {},
            collapsedAction = { },
            expandedIcon = { Icon(Icons.Default.ArrowUpward, null) },
            collapsedIcon = { Icon(Icons.Default.Search, null) },
        )
    }
}
