package com.kastik.apps.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnnouncementCardShimmer() {
    val shimmer = rememberShimmer()

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shimmer(shimmer),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBlock(width = 90.dp, height = 16.dp)
                ShimmerBlock(width = 60.dp, height = 14.dp)
            }

            Spacer(Modifier.height(12.dp))

            // Title
            ShimmerBlock(width = 220.dp, height = 22.dp)
            Spacer(Modifier.height(6.dp))
            ShimmerBlock(width = 140.dp, height = 22.dp)

            Spacer(Modifier.height(12.dp))

            // Categories chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    ShimmerBlock(
                        width = (60 + (it * 10)).dp,
                        height = 26.dp,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Preview text
            ShimmerBlock(width = Modifier.fillMaxWidth(), height = 14.dp)
            Spacer(Modifier.height(6.dp))
            ShimmerBlock(width = Modifier.fillMaxWidth(), height = 14.dp)
        }
    }
}

@Composable
private fun rememberShimmer(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val shift = transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing)
        ),
        label = "shimmer_shift"
    )

    return Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        ),
        start = Offset(shift.value, 0f),
        end = Offset(shift.value + 300f, 0f)
    )
}


private fun Modifier.shimmer(brush: Brush): Modifier = this.drawWithContent {
    drawContent()
    drawRect(brush)
}

@Composable
private fun ShimmerBlock(
    width: Any = Modifier.fillMaxWidth(),
    height: Dp,
    shape: Shape = RoundedCornerShape(6.dp)
) {
    val modifier = when (width) {
        is Dp -> Modifier.width(width)
        is Modifier -> width
        else -> Modifier.fillMaxWidth()
    }

    Box(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
    )
}


@Preview
@Composable
fun PreviewAnnouncementCardShimmer() {
    AnnouncementCardShimmer()
}