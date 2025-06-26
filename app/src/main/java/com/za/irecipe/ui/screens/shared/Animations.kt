package com.za.irecipe.ui.screens.shared

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DotsLoadingAnimation(
    dotCount: Int = 6,
    dotSize: Dp = 12.dp,
    dotColor: Color = MaterialTheme.colorScheme.primary,
    delayMillis: Int = 500
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    val animations = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = delayMillis * dotCount
                    1f at delayMillis * index with LinearEasing
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "dot-$index"
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        animations.forEach { anim ->
            Box(
                modifier = Modifier
                    .size(width = dotSize, height = dotSize)
                    .graphicsLayer {
                        alpha = anim.value
                    }
                    .background(color = dotColor, shape = CircleShape)
            )
        }
    }
}