package com.tearas.myapplication.presentation.show_media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.presentation.common.AnimNotFound
import com.tearas.myapplication.presentation.common.DetailMediaBottomSheet
import com.tearas.myapplication.presentation.common.DialogLoading
import com.tearas.myapplication.presentation.common.ListMedia
import com.tearas.myapplication.presentation.common.MediaParams
import com.tearas.myapplication.presentation.common.TopCenter
import com.tearas.myapplication.presentation.files_duplicated.component.ButtonRecovery
import com.tearas.myapplication.presentation.recovery_save.FilesSavedAction
import com.tearas.myapplication.presentation.show_media.component.MenuSort
import com.tearas.myapplication.utils.getTypeMedia
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAllMediaScreen(
    isRestore: Boolean = true,
    nameFolder: String,
    listMedia: List<MediaModel>,
    viewModel: ShowAllMediaViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.state

    var showDetail by remember { mutableStateOf(false) }

    var mediaModelClick by remember { mutableStateOf(MediaModel()) }
    var isShowDialogLoading by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val stateBottomSheet = rememberModalBottomSheetState(true)

    val scopes = rememberCoroutineScope()

    if (showDetail) {
        DetailMediaBottomSheet(
            sheetState = stateBottomSheet,
            mediaModel = mediaModelClick
        ) {
            scopes.launch { stateBottomSheet.hide() }.invokeOnCompletion {
                if (!stateBottomSheet.isVisible) {
                    showDetail = false
                }
            }
            showDetail = false
        }
    }
    if (isShowDialogLoading) {
        DialogLoading(
            angle = state.angle,
            title = stringResource(id = if (isRestore) R.string.files_recovering else R.string.files_deleting)
        ) { }
    }

    LaunchedEffect(key1 = state) {
        if (state.angle == 360f) {
            delay(1200)
            isShowDialogLoading = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setListMedia(listMedia)
    }
    Scaffold(topBar = {
        Column(horizontalAlignment = Alignment.End) {
            TopCenter(title = nameFolder, actions = {
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        painter = painterResource(id = R.drawable.sort),
                        contentDescription = null
                    )
                }
            }) { onBack() }

            MenuSort(isExpanded = isExpanded, onDismissRequest = {
                isExpanded = false
            }, onSort = {
                viewModel.onAction(ShowAllMediaAction.SortMedia(it))
                isExpanded = false
            })
        }
    }, bottomBar = {
        if (state.listMedia.isNotEmpty()) ButtonRecovery(
            Modifier.padding(20.dp),
            text = if (isRestore) stringResource(id = R.string.restore) else stringResource(id = R.string.delete),
            enabled = state.countItemSelected > 0,
            isSelected = state.countItemSelected > 0
        ) {
            if (isRestore) viewModel.onAction(ShowAllMediaAction.Recovery)
            else viewModel.onAction(ShowAllMediaAction.Delete)
            isShowDialogLoading = true
        }
    }) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (listMedia.isNotEmpty()) {
                val mediaParams by remember {
                    mutableStateOf(
                        MediaParams(mediaType = listMedia.first().typeMedia,
                            isShowChecked = true,
                            onClick = {
                                viewModel.onAction(ShowAllMediaAction.UpdateItemSelected(it))
                            },
                            onInfo = {
                                mediaModelClick = it
                                showDetail = true
                            })
                    )
                }
                ListMedia(
                    listMedia = state.listMedia, mediaParams = mediaParams
                )
            } else {
                AnimNotFound()
            }
        }
    }
}

@Preview(name = "ShowAllMediaScreen")
@Composable
private fun PreviewShowAllMediaScreen() {
    val viewModel: ShowAllMediaViewModel = hiltViewModel()

}