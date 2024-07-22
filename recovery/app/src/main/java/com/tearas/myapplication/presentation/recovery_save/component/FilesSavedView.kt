package com.tearas.myapplication.presentation.recovery_save.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.dto.FolderMediaDTO
import com.tearas.myapplication.presentation.common.AnimNotFound
import com.tearas.myapplication.presentation.common.ListMedia
import com.tearas.myapplication.presentation.common.MediaParams

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilesSavedView(
    modifier: Modifier = Modifier,
    map: Map<FolderMediaDTO, List<MediaModel>>,
    onMediaClick: (String, List<MediaModel>) -> Unit
) {
    val expandedStates = remember { mutableStateMapOf<FolderMediaDTO, Boolean>() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        map.forEach { (folderMediaDTO, mediaModels) ->
            stickyHeader {
                val isExpanded = expandedStates[folderMediaDTO] ?: false
                ItemFolderMedia(folderMediaDTO) {
                    onMediaClick(folderMediaDTO.title, map[folderMediaDTO]!!)
                }
            }
//            val mediaParams = MediaParams(
//                mediaType = folderMediaDTO.type,
//                isShowChecked = false,
//                onClick = { onMediaClick(it) },
//                onInfo = {}
//            )
//            item {
//                AnimatedVisibility(
//                    visible = expandedStates[folderMediaDTO] == true,
//                    enter = expandIn(
//                        animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing),
//                        expandFrom = Alignment.BottomCenter,
//                        initialSize = { IntSize(width = it.width, height = 0) }
//                    ) + fadeIn(tween(300)),
//                    exit = shrinkVertically(
//                        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
//                        shrinkTowards = Alignment.Bottom,
//                    ) + fadeOut(tween(300))
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .heightIn(0.dp, 300.dp)
//                            .fillMaxWidth()
//                    ) {
//                        if (mediaModels.isNotEmpty()) ListMedia(
//                            modifier = Modifier,
//                            listMedia = mediaModels,
//                            mediaParams = mediaParams,
//                        ) else AnimNotFound()
//                    }
//                }
//            }
        }
    }
}


@Preview(name = "FilesSavedView")
@Composable
private fun PreviewFileRecoverySavedView() {
}