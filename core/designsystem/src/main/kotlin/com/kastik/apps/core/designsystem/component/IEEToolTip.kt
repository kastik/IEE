package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import kotlinx.coroutines.launch

@Composable
fun IEEIconToolTip(
    modifier: Modifier = Modifier,
    tooltipTitle: @Composable () -> Unit,
    tooltipBody: @Composable () -> Unit,
    icon: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState(
        isPersistent = true
    )

    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above,
            16.dp
        ),
        tooltip = {
            RichTooltip(
                title = tooltipTitle
            ) {
                tooltipBody()
            }
        },
        state = tooltipState
    ) {
        IconButton(onClick = {
            scope.launch {
                tooltipState.show()
            }
        }) {
            icon()
        }
    }
}

@Composable
fun IEESliderThumbToolTip(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    tooltipText: String,
) {
    val tooltipState = rememberTooltipState(
        isPersistent = true
    )

    val isDragged by interactionSource.collectIsDraggedAsState()
    LaunchedEffect(isDragged) {
        if (isDragged) {
            tooltipState.show()
        } else {
            tooltipState.dismiss()
        }
    }

    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above,
            12.dp
        ),
        tooltip = {
            RichTooltip {
                Text(
                    text = tooltipText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontFeatureSettings = "tnum"
                    ),
                )
            }
        },
        state = tooltipState
    ) {
        SliderDefaults.Thumb(
            interactionSource = interactionSource,
            colors = SliderDefaults.colors(),
            enabled = enabled,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IEEIconToolTipPreview() {
    AppsAboardTheme {
        IEEIconToolTip(
            tooltipTitle = { Text(text = "Tooltip Title") },
            tooltipBody = { Text(text = "This is the body of the tooltip.") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IEESliderThumbToolTipPreview() {
    AppsAboardTheme {
        IEESliderThumbToolTip(
            enabled = true,
            interactionSource = remember { MutableInteractionSource() },
            tooltipText = "50"
        )
    }
}
