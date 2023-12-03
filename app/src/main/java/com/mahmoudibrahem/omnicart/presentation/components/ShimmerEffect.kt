package com.mahmoudibrahem.omnicart.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun shimmerBrush():Brush {
    val transition = rememberInfiniteTransition(label = "")
    val transitionAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        label = "",
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    return Brush.linearGradient(
        colors = listOf(
            Color(0xFFD1D1D1).copy(alpha = 0.8f),
            Color(0xFFD1D1D1).copy(alpha = 0.4f),
            Color(0xFFD1D1D1).copy(alpha = 0.8f)
        ),
        start = Offset.Zero,
        end = Offset(x = transitionAnim.value, y = transitionAnim.value)
    )
}
