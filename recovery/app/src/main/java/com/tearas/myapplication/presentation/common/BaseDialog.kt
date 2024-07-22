package com.tearas.myapplication.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.tearas.myapplication.R

@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = {
            Text(
                text = body
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(stringResource(id = R.string.delete), color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}