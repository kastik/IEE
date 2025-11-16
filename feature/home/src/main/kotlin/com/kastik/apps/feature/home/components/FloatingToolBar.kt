package com.kastik.apps.feature.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalAnimationApi::class)
@Composable
fun FloatingToolBar(
    expanded: Boolean,
    onFabClick: () -> Unit,
    nextPage: () -> Unit,
    prevPage: () -> Unit,
) {
    HorizontalFloatingToolbar(
        expanded = expanded,
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = onFabClick
            ) {
                AnimatedContent(
                    targetState = expanded,
                    transitionSpec = {
                        ((fadeIn() + scaleIn(initialScale = 1f)).togetherWith(
                            fadeOut() + scaleOut(
                                targetScale = 1f
                            )
                        ))
                            .using(SizeTransform(clip = false))
                    }
                ) { isExpanded ->
                    val icon = if (isExpanded) {
                        Icons.Filled.Search
                    } else {
                        Icons.Filled.ArrowUpward
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            }
        },
        content = {
            IconButton(onClick = nextPage) {
                Icon(Icons.AutoMirrored.Default.ArrowLeft, contentDescription = null)
            }
            VerticalDivider(Modifier.height(12.dp))
            IconButton(onClick = prevPage) {
                Icon(Icons.AutoMirrored.Default.ArrowRight, contentDescription = null)
            }
        },
    )
}