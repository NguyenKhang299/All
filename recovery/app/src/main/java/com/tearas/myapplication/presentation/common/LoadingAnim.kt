package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.tearas.myapplication.R

@Composable
fun LoadingAnim(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.anim_scan)
    )

    LottieAnimation(
        isPlaying = isPlaying,
        composition = composition,
        modifier = modifier
            .aspectRatio(1f)
             ,
        iterations = LottieConstants.IterateForever,
        speed = 2f
    )
}

@Preview(name = "LoadingAnim")
@Composable
private fun PreviewLoadingAnim() {
    LoadingAnim()
}