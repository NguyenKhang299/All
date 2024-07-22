package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieAnimationView
import com.tearas.myapplication.R

@Composable
fun AnimNotFound() {
    val context = LocalContext.current
    val customView = remember { LottieAnimationView(context) }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_data),
                modifier = Modifier.size(80.dp),
                contentDescription = null
            )
            Text(
                text = stringResource(id = R.string.no_data),
                fontStyle = FontStyle.Italic,
                color = Color.LightGray
            )
        }
    }
}