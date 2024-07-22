package com.tearas.myapplication.presentation.common

import android.content.Intent
import android.text.format.Formatter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.utils.shareMedia
import java.io.File
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMediaBottomSheet(
    sheetState: SheetState,
    mediaModel: MediaModel,
    isShowDelete: Boolean = false,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val mediaType = mediaModel.typeMedia
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(15.dp)
            ) {

                TextInfo(label = stringResource(id = R.string.name)) {
                    Text(text = mediaModel.name, style = MaterialTheme.typography.labelMedium)
                }
                SpaceVertical(height = 10.dp)
                TextInfo(label = stringResource(id = R.string.path)) {
                    Text(text = mediaModel.path, style = MaterialTheme.typography.labelMedium)
                }
                SpaceVertical(height = 10.dp)
                TextInfo(label = stringResource(id = R.string.size)) {
                    Text(
                        text = Formatter.formatFileSize(context, mediaModel.size),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                SpaceVertical(height = 10.dp)
                TextInfo(label = stringResource(id = R.string.type)) {
                    Text(
                        text = mediaModel.mime,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                SpaceVertical(height = 10.dp)
                TextInfo(label = stringResource(id = R.string.date)) {
                    Text(
                        text = Date(mediaModel.date).toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                SpaceVertical(height = 10.dp)
                if (mediaType == MediaType.IMAGE || mediaType == MediaType.VIDEO) {
                    AsyncImageVideo(
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        enabled = false,
                        media = mediaModel,
                        mediaType = mediaType
                    )
                }
                if (isShowDelete) Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.delete),
                        style = MaterialTheme.typography.titleMedium, color = Color.White
                    )
                }
            }
            IconButton(onClick = {
                context.shareMedia(mediaModel.path)
            }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
            }
        }
    }
}

@Composable
fun TextInfo(label: String, content: @Composable RowScope.() -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = label + " : ", style = MaterialTheme.typography.titleSmall)
        content()
    }
}