package com.kastik.apps.core.designsystem.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun IeeIconToolTip(
    modifier: Modifier = Modifier,
    tooltipTitle: @Composable () -> Unit,
    tooltipBody: @Composable () -> Unit,
    icon: @Composable () -> Unit,
) {
    val vibrator = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState(isPersistent = true)

    TooltipBox(
        modifier = modifier,
        positionProvider =
            TooltipDefaults.rememberTooltipPositionProvider(
                TooltipAnchorPosition.Above,
                16.dp,
            ),
        tooltip = {
            RichTooltip(title = tooltipTitle) {
                tooltipBody()
            }
        },
        state = tooltipState,
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    vibrator.performHapticFeedback(HapticFeedbackType.ContextClick)
                    tooltipState.show()
                }
            }
        ) {
            icon()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IeeIconToolTipPreview() {
    IeePreview {
        IeeIconToolTip(
            tooltipTitle = { Text(text = "Tooltip Title") },
            tooltipBody = { Text(text = "This is the body of the tooltip.") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                )
            },
        )
    }
}
