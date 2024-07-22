package com.tearas.myapplication.presentation.scan

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.ListItemScanResults
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.presentation.common.AnimNotFound
import com.tearas.myapplication.presentation.common.ItemScanResults
import com.tearas.myapplication.presentation.common.MediaParams
import com.tearas.myapplication.presentation.common.MediaStoreView
import com.tearas.myapplication.presentation.common.ScannerMedia
import com.tearas.myapplication.presentation.common.SpaceVertical
import com.tearas.myapplication.presentation.graph.Route
import com.tearas.myapplication.utils.getTypeMedia

@Composable
fun ScanScreen(
    modifier: Modifier = Modifier,
    optionEnum: OptionEnum = OptionEnum.SCANNER_VIDEO,
    viewModel: ScanViewModel,
    onDestination: (String, List<MediaModel>) -> Unit,
    onBack: () -> Unit
) {
    val state = viewModel.state

    var isStart by remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.isSuccess) {
        isStart = !(state.isSuccess)
    }
    val mediaType = optionEnum.getTypeMedia()
    ScannerMedia(
        modifier = modifier.background(Color(0xFFebeef5)),
        isStart = isStart,
        isSuccess = state.isSuccess,
        title = stringResource(id = ListItemScanResults[mediaType]!!.title),
        fileScanner = state.onPathFile,
        onStart = {
            viewModel.handleScan(optionEnum)
        },
        onBack = { onBack() }) {

        SpaceVertical()
        ListItemScanResults[mediaType]?.apply {
            ItemScanResults(
                title = stringResource(id = title),
                icon = icon,
                filesSize = state.mapImages.values.flatten().size,
                body = stringResource(id = mediaFound),
                description = stringResource(id = description),
            ) {
                viewModel.handleScan(optionEnum)
            }
        }

        SpaceVertical()

        if (state.isSuccess && state.mapImages.isNotEmpty()) {
            val mediaParams = MediaParams(
                mediaType = mediaType,
                isShowChecked = false,
                onClick = { },
                onInfo = { }
            )
            MediaStoreView(
                map = state.mapImages,
                mediaParams = mediaParams,
                userScrollEnabled = false
            ) { nameFolder, listMedia ->
                onDestination(nameFolder, listMedia)
            }
        } else {
            AnimNotFound()
        }
    }
}


@Preview(name = "ScanScreen")
@Composable
private fun PreviewScannerImageScreen() {
    val viewModel: ScanViewModel = hiltViewModel()

}