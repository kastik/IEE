package com.kastik.apps.core.designsystem.component

import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun IeeLinearWavyProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = WavyProgressIndicatorDefaults.indicatorColor, //MaterialTheme.colorScheme.primary
    trackColor: Color = WavyProgressIndicatorDefaults.trackColor, //Color.Transparent,
    stroke: Stroke = WavyProgressIndicatorDefaults.linearIndicatorStroke,
    trackStroke: Stroke = WavyProgressIndicatorDefaults.linearTrackStroke,
) {
    LinearWavyProgressIndicator(
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        stroke = stroke,
        trackStroke = trackStroke
    )
}


@Preview
@Composable
fun IeeLinearWavyProgressIndicatorPreview() {
    IeePreview {
        IeeLinearWavyProgressIndicator()
    }
}



