package com.tearas.myapplication.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tearas.myapplication.R

@Composable
fun PermissionExplanationDialogHigherSdk30(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(R.string.storage_access_permission_title)) },
        text = {
            Text(text = stringResource(R.string.storage_access_permission_body_highersdk30))
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.grant_permission))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}