package com.tearas.myapplication.presentation.recovery_save

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.presentation.common.AnimNotFound
import com.tearas.myapplication.presentation.common.DialogLoading
import com.tearas.myapplication.presentation.common.FloatingActionButtonDelete
import com.tearas.myapplication.presentation.common.TopCenter
import com.tearas.myapplication.presentation.recovery_save.component.DialogDeleteFilesRecoverySaved
import com.tearas.myapplication.presentation.recovery_save.component.FilesSavedView
import com.tearas.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay


@Composable
fun FilesSavedScreen(
    viewModel: FilesSavedViewModel,
    onDestination: (String, List<MediaModel>) -> Unit,
    onBack: () -> Unit
) {
    val state = viewModel.state
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    var isShowDialogLoading by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(FilesSavedAction.GetFilesSaved)
    }
    if (isShowDeleteDialog) DialogDeleteFilesRecoverySaved(onDismiss = {
        isShowDeleteDialog = false
    }, onConfirm = {
        isShowDialogLoading = true
        isShowDeleteDialog = false
        viewModel.onAction(FilesSavedAction.DeleteFile)
    })

    if (isShowDialogLoading) {
        DialogLoading(angle = state.angle, title = stringResource(id = R.string.files_deleting)) { }
    }

    LaunchedEffect(key1 = state) {
        if (state.angle == 360f) {
            delay(1200)
            isShowDialogLoading = false
        }
    }

    Scaffold(topBar = {
        TopCenter(title = "Files Recovery Saved") {
            onBack()
        }
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isSuccess != true && state.mapFolder.isEmpty() -> {
                    CircularProgressIndicator()
                }

                state.isSuccess == true && state.mapFolder.isEmpty() -> {
                    AnimNotFound()
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        FilesSavedView(map = state.mapFolder, onMediaClick = onDestination)

                        if (state.countMediaSelected > 0) FloatingActionButtonDelete(
                            modifier = Modifier.padding(15.dp),
                            description = "${state.countMediaSelected} files selected"
                        ) {
                            isShowDeleteDialog = true
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun FileRecoveryScreenPre() {
    MyApplicationTheme {

    }
}