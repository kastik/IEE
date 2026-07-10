package com.kastik.apps.core.ui.paging

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.coroutines.cancellation.CancellationException

class PagerPredictiveBackState(
    val scale: Float,
    val translationX: Float,
    val alpha: Float,
    val isProcessing: Boolean
)

@Composable
fun rememberPagerPredictiveBackState(
    pagerState: PagerState,
    maxShrink: Float = 0.15f,
    maxTranslationX: Dp = 48.dp,
    maxAlphaReduction: Float = 0.3f,
    animationDurationMs: Int = 300,
    onBack: () -> Unit
): PagerPredictiveBackState {
    var backEventProgress by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val maxTranslationPx = remember(maxTranslationX, density) {
        with(density) { maxTranslationX.toPx() }
    }

    PredictiveBackHandler(enabled = pagerState.currentPage > 0) { progressFlow ->
        try {
            progressFlow.collect { backEvent ->
                backEventProgress = backEvent.progress
            }
            backEventProgress = 0f
            onBack()
        } catch (e: CancellationException) {
            backEventProgress = 0f
        }
    }

    val isSwiping = backEventProgress > 0f
    val animSpec = tween<Float>(if (isSwiping) 0 else animationDurationMs)

    val scale by animateFloatAsState(
        targetValue = 1f - (backEventProgress * maxShrink),
        animationSpec = animSpec,
        label = "PredictiveBackScale"
    )
    val translationX by animateFloatAsState(
        targetValue = backEventProgress * maxTranslationPx,
        animationSpec = animSpec,
        label = "PredictiveBackTranslation"
    )
    val alpha by animateFloatAsState(
        targetValue = 1f - (backEventProgress * maxAlphaReduction),
        animationSpec = animSpec,
        label = "PredictiveBackAlpha"
    )

    return remember(scale, translationX, alpha, isSwiping) {
        PagerPredictiveBackState(
            scale = scale,
            translationX = translationX,
            alpha = alpha,
            isProcessing = isSwiping
        )
    }
}