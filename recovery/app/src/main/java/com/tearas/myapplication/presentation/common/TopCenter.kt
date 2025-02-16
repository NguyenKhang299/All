package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopCenter(
    modifier: Modifier = Modifier,
    title: String,
    actions: @Composable() (RowScope.() -> Unit) = {},
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(modifier = modifier, title = {
        Text(text = title)
    }, navigationIcon = {
        IconButton(onClick = { onBack() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }, actions = actions)
}

@Preview(name = "TopCenter")
@Composable
private fun PreviewTopCenter() {
    TopCenter(title = "Top Center") {

    }
}