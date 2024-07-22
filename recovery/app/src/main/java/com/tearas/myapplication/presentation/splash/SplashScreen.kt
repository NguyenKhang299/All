package com.tearas.myapplication.presentation.splash

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tearas.myapplication.R
import com.tearas.myapplication.presentation.graph.Route
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    destination: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var expandedText by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        expanded = true
        delay(300)
        expandedText = true
        delay(3000)
        destination(Route.HomeScreen.route)
    }

    val textMeasurer = rememberTextMeasurer()
    val text = if (expandedText) stringResource(id = R.string.app_name) else ""

    val styleText = MaterialTheme.typography.titleMedium.copy(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )

    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = styleText
    )

    val width: Dp = with(LocalDensity.current) {
        if (expandedText) {
            textLayoutResult.size.width.toDp()
        } else {
            0.dp
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .height(if (expanded) 280.dp else 0.dp)
                .alpha(if (expanded) 1f else 0f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.clear),
                contentDescription = null,
                modifier = Modifier
                    .width(250.dp)
                    .height(100.dp),
                contentScale = ContentScale.Inside
            )
            Text(
                text = text,
                color = Color.Black,
                style = styleText,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .animateContentSize()
                    .width(if (expandedText) width else 0.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}