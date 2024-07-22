package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SpaceHorizontal(
    modifier: Modifier = Modifier,
    width: Dp = 15.dp,
) {
    Spacer(modifier = modifier.width(width ))
}