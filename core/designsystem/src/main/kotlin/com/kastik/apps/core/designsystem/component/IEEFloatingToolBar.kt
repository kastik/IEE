package com.kastik.apps.core.designsystem.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IEEFloatingToolBar(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    expandedAction: () -> Unit,
    collapsedAction: () -> Unit,
    expandedIcon: @Composable () -> Unit,
    collapsedIcon: @Composable () -> Unit,
    collapsedSecondaryIcons: (@Composable () -> Unit)? = null,
) {
    val vibrator = LocalHapticFeedback.current

    HorizontalFloatingToolbar(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        expanded = expanded,
        colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
        contentPadding = if (collapsedSecondaryIcons == null) PaddingValues(0.dp) else PaddingValues(
            horizontal = 16.dp
        ),
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = {
                    vibrator.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                    if (expanded) collapsedAction() else expandedAction()
                }
            ) {
                Crossfade(targetState = expanded, label = "fab-icon") { isExpanded ->
                    if (isExpanded) collapsedIcon() else expandedIcon()
                }
            }
        }
    ) {
        collapsedSecondaryIcons?.let {
            it()
        }
    }
}

@Preview
@Composable
fun IEEFloatingToolBarExpandedPreview() {
    MaterialTheme {
        IEEFloatingToolBar(
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
fun IEEFloatingToolBarCollapsedPreview() {
    MaterialTheme {
        IEEFloatingToolBar(
            expanded = true,
            expandedAction = {},
            collapsedAction = { },
            expandedIcon = { Icon(Icons.Default.ArrowUpward, null) },
            collapsedIcon = { Icon(Icons.Default.Search, null) },
        )
    }
}
