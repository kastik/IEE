package com.kastik.apps.core.designsystem.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

fun <T> ieeListSpring() = spring<T>(
    stiffness = Spring.StiffnessLow,
    dampingRatio = Spring.DampingRatioLowBouncy
)