package com.tearas.myapplication.presentation.home.component

import android.text.format.Formatter
import android.util.Log
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.presentation.common.AutoResizedText
import com.tearas.myapplication.presentation.common.BrushGradient
import com.tearas.myapplication.ui.theme.Background_Light
import com.tearas.myapplication.utils.StatUtils
import org.jetbrains.annotations.Range

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    angle: Float = 0f,
    size: Dp = 120.dp,
    chartBarWidth: Dp = 10.dp,
    textContent: @Composable (() -> Unit)
) {

    var animationPlayed by remember { mutableStateOf(false) }

    val animate by animateFloatAsState(
        targetValue = if (animationPlayed) angle else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = modifier
            .size(size)
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawArc(
                color = Background_Light,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                brush = BrushGradient,
                startAngle = -90f,
                sweepAngle = animate,
                useCenter = false,
                style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = chartBarWidth)
        ) {
            textContent()
        }
    }
}

@Preview(name = "PieChart")
@Composable
private fun PreviewPieChart() {
    PieChart() {}
}