package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SpaceVertical(
    modifier: Modifier = Modifier,
    height: Dp = 15.dp,
) {
    Spacer(modifier = modifier.height(height))
}

@Preview(name = "SpaceVertical")
@Composable
private fun PreviewSpaceVertical() {
    SpaceVertical()
}