package com.tearas.myapplication.presentation.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.tearas.myapplication.ui.theme.Blue_Light

val BrushGradient: Brush = Brush.verticalGradient(
    colors = listOf(Color.Cyan, Blue_Light)
)
val BrushGradientText: Brush = Brush.verticalGradient(
    colors = listOf(Color.Red, Blue_Light)
)

@Composable
fun Modifier.backgroundGradient() = composed {
    background(
        brush = BrushGradient, shape = CircleShape
    )
}

fun Modifier.shimmerEffect() = composed {
    var isStart by remember {
        mutableStateOf(false)
    }
    val anim by animateFloatAsState(
        targetValue = if (!isStart) 0.5f else 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearOutSlowInEasing
        ), label = ""
    )
    LaunchedEffect(key1 = Unit) {
        isStart = true
    }
    graphicsLayer {
        alpha = anim
    }
}

@Composable
fun Modifier.rotateInfinite() = composed {
    val rotation = rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    ).value
    rotate(rotation)
}

@Preview(name = "BackGroundGradient")
@Composable
private fun PreviewBackGroundGradient() {

}