package com.tearas.myapplication.presentation.common

import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerMedia(
    modifier: Modifier = Modifier,
    title: String = "Title",
    fileScanner: String = "",
    isStart: Boolean = true,
    isSuccess: Boolean = false,
    onStart: () -> Unit,
    onBack: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val stateText = listOf(
        stringResource(id = R.string.press_to_start_scan),
        stringResource(id = R.string.scan_will_take_a_few_seconds),
    )

    var isClick by remember {
        mutableStateOf(false)
    }
    var isPlaying by remember {
        mutableStateOf(false)
    }
    var textState by remember {
        mutableStateOf("")
    }
    LaunchedEffect(fileScanner, isClick, Unit) {
        if (fileScanner.isNotEmpty()) {
            textState = fileScanner
        } else {
            if (isClick) {
                textState = stateText[1]
                delay(1000)
                onStart()
            } else {
                textState = stateText[0]
            }
        }
    }
    Scaffold(
        topBar = {
            TopCenter(title = title) { onBack() }
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (isSuccess && !isStart) {
                content()
            } else {
                ContentScanner(textState, isPlaying) {
                    if (!isPlaying) {
                        isPlaying = true
                        isClick = true
                    }
                }
            }
        }
    }
}

@Composable
fun ContentScanner(
    text: String,
    isPlaying: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                enabled = !isPlaying,
                interactionSource = null,
                indication = rememberRipple()
            ) { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingAnim(
            modifier = Modifier.rotateInfinite()
                .fillMaxWidth(0.5f)
                .aspectRatio(1f), isPlaying = isPlaying
        )
        Text(
            modifier = Modifier.padding(horizontal = 15.dp),
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            minLines = 2
        )
    }
}


@Preview(name = "ScannerMedia")
@Composable
private fun PreviewScannerMedia() {
    ScannerMedia(onStart = {}, onBack = {}) {

    }
}