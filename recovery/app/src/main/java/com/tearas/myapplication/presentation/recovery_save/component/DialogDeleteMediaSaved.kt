package com.tearas.myapplication.presentation.recovery_save.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.tearas.myapplication.R
import com.tearas.myapplication.presentation.common.BaseDialog

@Composable
fun DialogDeleteFilesRecoverySaved(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BaseDialog(
        title = stringResource(R.string.delete_files_recovery_saved_title),
        body = stringResource(R.string.delete_files_recovery_saved_body),
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}


@Preview(name = "DialogDeleteMediaDuplicated")
@Composable
private fun PreviewDialogDeleteMediaDuplicated() {
    DialogDeleteFilesRecoverySaved(onDismiss = {}, onConfirm = {})
}