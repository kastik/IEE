package com.kastik.apps.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IeeSliderThumbToolTip(
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
private fun IeeSliderThumbToolTipPreview() {
    IeePreview {
        IeeSliderThumbToolTip(
            enabled = true,
            interactionSource = remember { MutableInteractionSource() },
            tooltipText = "50"
        )
    }
}