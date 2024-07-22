package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType

@Composable
fun MediaStoreView(
    modifier: Modifier = Modifier,
    map: Map<FolderModel, List<MediaModel>>,
    mediaParams: MediaParams,
    userScrollEnabled: Boolean = false,
    onClickShowAll: (String, List<MediaModel>) -> Unit
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        map.forEach { (folderModel, mediaModels) ->
            item {
                ItemHeaderMediaStore(
                    folder = folderModel, listMedia = mediaModels, onClick = {
                        onClickShowAll(folderModel.name, mediaModels)
                    }
                )
            }

            item {
                ListMedia(
                    listMedia = mediaModels.take(4),
                    mediaParams = mediaParams,
                    userScrollEnabled = userScrollEnabled
                )
            }
        }
    }
}

@Composable
fun ItemHeaderMediaStore(
    modifier: Modifier = Modifier,
    folder: FolderModel,
    listMedia: List<MediaModel>,
    onClick: (FolderModel) -> Unit
) {
    CardElevatedItem {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.folder_media_info, folder.name, listMedia.size),
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            ButtonGradient(onClick = { onClick(folder) }, enabled = true) {
                Text(
                    text = stringResource(id = R.string.show_all),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(name = "ListChildFordel")
@Composable
private fun PreviewListChildFolder() {

}