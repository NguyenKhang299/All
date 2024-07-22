package com.tearas.myapplication.presentation.files_duplicated

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.presentation.common.AnimNotFound
import com.tearas.myapplication.presentation.common.DetailMediaBottomSheet
import com.tearas.myapplication.presentation.common.DialogLoading
import com.tearas.myapplication.presentation.common.ScannerMedia
import com.tearas.myapplication.presentation.files_duplicated.component.DialogDeleteMediaDuplicated
import com.tearas.myapplication.presentation.common.FloatingActionButtonDelete
import com.tearas.myapplication.presentation.files_duplicated.component.MediaDuplicatesView
import com.tearas.myapplication.presentation.files_duplicated.component.TabFilesDuplicated
import com.tearas.myapplication.presentation.files_duplicated.component.tabItems
import com.tearas.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilesDuplicatedScreen(
    modifier: Modifier = Modifier,
    viewModel: FilesDuplicateViewModel,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(0)
    val scope = rememberCoroutineScope()
    var tabSelected by remember { mutableIntStateOf(0) }
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    var isShowDialogLoading by remember { mutableStateOf(false) }
    var showDetail by remember { mutableStateOf(false) }
    var mediaModelClick by remember { mutableStateOf(MediaModel()) }
    val stateBottomSheet = rememberModalBottomSheetState(true)
    val scopes = rememberCoroutineScope()

    val state = viewModel.state

    if (isShowDeleteDialog) DialogDeleteMediaDuplicated(onDismiss = {
        isShowDeleteDialog = false
    }, onConfirm = {
        isShowDialogLoading = true
        isShowDeleteDialog = false
        viewModel.onAction(FilesDuplicatedAction.DeleteFile)
    })

    if (isShowDialogLoading) {
        DialogLoading(angle = state.angle, title = "Files Deleting...") { }
    }
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
    LaunchedEffect(key1 = state) {
        if (state.angle == 360f) {
            delay(1200)
            isShowDialogLoading = false
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        if (tabSelected != pagerState.currentPage) tabSelected = pagerState.currentPage
    }

    ScannerMedia(
        modifier = modifier.background(Color(0xFFebeef5)),
        isStart = state.isStart,
        isSuccess = state.isSuccess,
        title = stringResource(id = R.string.files_duplicated),
        fileScanner = state.onPathFile,
        onStart = {
            viewModel.onAction(FilesDuplicatedAction.ScanFilesDuplicated)
        },
        onBack = { onBack() }) {

        TabFilesDuplicated(
            selectedTabIndex = tabSelected,
            tabItems = tabItems
        ) { page ->
            tabSelected = page
            scope.launch { pagerState.animateScrollToPage(page) }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            HorizontalPager(
                state = pagerState,
                count = tabItems.size,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val listSet = when (page) {
                    0 -> state.mapSet.values.flatten()
                    1 -> state.mapSet[MediaType.IMAGE]
                    2 -> state.mapSet[MediaType.VIDEO]
                    3 -> state.mapSet[MediaType.AUDIO]
                    4 -> state.mapSet[MediaType.FILES]
                    5 -> state.mapSet[MediaType.APK]
                    else -> state.mapSet[MediaType.IMAGE]
                }
                if (state.isSuccess && listSet?.isNotEmpty() == true) {
                    MediaDuplicatesView(
                        isShowInfo = true,
                        modifier = Modifier.fillMaxSize(),
                        listSet = listSet,
                        onClickMedia = { idSet, media ->
                            viewModel.onAction(
                                FilesDuplicatedAction.UpdateMediaClick(
                                    idSet, media
                                )
                            )
                        }, onSelectedAll = { isSelected, idSet ->
                            viewModel.onAction(
                                FilesDuplicatedAction.UpdateSetClick(
                                    idSet,
                                    isSelected
                                )
                            )
                        }, onClickInfo = {
                            mediaModelClick = it
                            showDetail = true
                        }
                    )
                } else AnimNotFound()
            }
            if (state.countMediaSelected != 0) FloatingActionButtonDelete(
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.BottomEnd),
                description = stringResource(R.string.items_selected, state.countMediaSelected),
                onClick = { isShowDeleteDialog = true },
            )
        }
    }
}

@Preview(name = "FilesDuplicatedScreen")
@Composable
private fun PreviewFilesDuplicatedScreen() {
    MyApplicationTheme {
        val viewModel: FilesDuplicateViewModel = hiltViewModel()
        FilesDuplicatedScreen(viewModel = viewModel) {}
    }
}