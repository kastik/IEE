package com.kastik.apps.core.ui.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import com.kastik.apps.core.ui.paging.PagerPredictiveBackState

fun Modifier.predictiveBackPagerEffect(
    page: Int,
    currentPage: Int,
    backState: PagerPredictiveBackState
): Modifier = this.then(
    Modifier.graphicsLayer {
        if (page == currentPage) {
            transformOrigin = TransformOrigin(0.5f, 0.5f)
            scaleX = backState.scale
            scaleY = backState.scale
            translationX = backState.translationX
            alpha = backState.alpha
        }
    }
)