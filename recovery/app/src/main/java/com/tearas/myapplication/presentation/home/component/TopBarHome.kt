package com.tearas.myapplication.presentation.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tearas.myapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome(
    modifier: Modifier = Modifier
) {
    TopAppBar(title = {
        Text(text = stringResource(id = R.string.app_name))
    },
        actions = {
            
        })
}

@Preview(name = "TopBarHome")
@Composable
private fun PreviewTopBarHome() {
    TopBarHome()
}