package com.tearas.myapplication.presentation.files_duplicated.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.SetModel
import com.tearas.myapplication.presentation.common.ButtonGradient
import com.tearas.myapplication.presentation.common.ListMedia
import com.tearas.myapplication.presentation.common.MediaParams
import com.tearas.myapplication.presentation.common.backgroundGradient
import com.tearas.myapplication.ui.theme.MyApplicationTheme

@Composable
fun MediaDuplicatesView(
    modifier: Modifier = Modifier,
    listSet: List<SetModel>,
    isShowInfo: Boolean = false,
    onSelectedAll: (Boolean, String) -> Unit,
    onClickMedia: (String, MediaModel) -> Unit,
    onClickInfo: (MediaModel) -> Unit
) {
    LazyColumn(modifier) {
        listSet.forEachIndexed { index, setModel ->
            item {
                var isSelectedAll by remember {
                    mutableStateOf(false)
                }
                isSelectedAll = setModel.listMedia.all { it.isSelected }
                ItemHeaderDuplicate(
                    isSelectedAll, index, setModel
                ) { isSelected ->
                    onSelectedAll(isSelected, setModel.id)
                    isSelectedAll = isSelected
                }
            }
            setModel.listMedia.chunked(12).forEach { mediaModels ->
                item {
                    val mediaParams = MediaParams(
                        mediaType = mediaModels.first().typeMedia,
                        isShowChecked = isShowInfo,
                        onClick = { mediaModel -> onClickMedia(setModel.id, mediaModel) },
                        onInfo = { media -> onClickInfo(media) }
                    )
                    ListMedia(
                        listMedia = mediaModels,
                        mediaParams = mediaParams,
                        userScrollEnabled = false
                    )
                }
            }
        }
    }
}

@Composable
fun ItemHeaderDuplicate(
    isSelectedAll: Boolean = false,
    index: Int,
    setModel: SetModel,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.set_and_items, index + 1, setModel.listMedia.size),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            ButtonGradient(
                enabled = true,
                isSelected = isSelectedAll,
                onClick = { onCheckedChange(!isSelectedAll) }) {
                Text(
                    text = "Select All",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewMediaStoreDuplicateView() {
    MyApplicationTheme {

    }
}