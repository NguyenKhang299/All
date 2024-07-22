package com.tearas.myapplication.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberImagePainter
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.ListItemScanResults
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.ui.theme.Background_Light
import java.text.SimpleDateFormat

data class MediaParams(
    val mediaType: MediaType,
    val isShowChecked: Boolean = true,
    val onClick: (MediaModel) -> Unit,
    val onInfo: (MediaModel) -> Unit
)

@Composable
fun ListMedia(
    modifier: Modifier = Modifier,
    listMedia: List<MediaModel>,
    userScrollEnabled: Boolean = true,
    mediaParams: MediaParams
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val screenHeightDp = configuration.screenHeightDp
    if (mediaParams.mediaType == MediaType.IMAGE || mediaParams.mediaType == MediaType.VIDEO) {
        LazyVerticalGrid(
            userScrollEnabled = userScrollEnabled,
            columns = GridCells.Fixed(4),
            modifier = modifier
                .padding(5.dp)
                .fillMaxSize()
                .heightIn(min = 0.dp, max = screenHeightDp.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(listMedia) { media ->
                ItemMedia(
                    modifier = modifier.fillMaxWidth(),
                    media = media,
                    mediaParams = mediaParams
                )
            }
        }
    } else {
        LazyColumn(
            userScrollEnabled = userScrollEnabled,
            modifier = modifier
                .padding(5.dp)
                .fillMaxSize()
                .heightIn(min = 0.dp, max = screenHeightDp.dp),
        ) {
            items(listMedia, key = { it.id }) { media ->
                ItemMedia(
                    modifier = modifier.fillMaxWidth(),
                    media = media,
                    mediaParams = mediaParams
                )
            }
        }
    }
}

@Composable
fun ItemMedia(
    modifier: Modifier = Modifier,
    media: MediaModel,
    mediaParams: MediaParams
) {
    if (mediaParams.mediaType == MediaType.IMAGE || mediaParams.mediaType == MediaType.VIDEO) {
        ItemMediaRow(modifier, media, mediaParams)
    } else {
        ItemMediaColumn(modifier, media, mediaParams)
    }
}

@Composable
fun ItemMediaRow(
    modifier: Modifier,
    media: MediaModel,
    mediaParams: MediaParams
) {
    mediaParams.apply {
        Column(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.TopEnd) {

                AsyncImageVideo(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, BrushGradient, RoundedCornerShape(10.dp))
                        .background(Color.White), enabled = true,
                    media, mediaType, onInfo
                )
                if (isShowChecked) ViewChecked(isSelected = media.isSelected) {
                    onClick(media)
                }
            }
            Text(
                text = media.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ViewChecked(isSelected: Boolean, onClick: () -> Unit) {
    if (isSelected) Image(
        painter = painterResource(
            id = R.drawable.ic_selected
        ),
        contentDescription = null,
        modifier = Modifier
            .padding(5.dp)
            .size(20.dp)
            .clickable { onClick() },
    ) else {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .size(20.dp)
                .background(color = Background_Light, shape = CircleShape)
                .clip(CircleShape)
                .clickable { onClick() },
        )
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun ItemMediaColumn(
    modifier: Modifier,
    media: MediaModel,
    mediaParams: MediaParams
) {
    val context = LocalContext.current

    mediaParams.apply {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onInfo(media) }
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            val (image, name, date, endView) = createRefs()

            Image(
                modifier = Modifier
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                    }
                    .size(35.dp),
                painter = if (media.typeMedia == MediaType.APK && media.appIcon != null) BitmapPainter(
                    media.appIcon.toBitmap().asImageBitmap()
                ) else painterResource(
                    id = (ListItemScanResults[media.typeMedia]
                        ?: ListItemScanResults[MediaType.FILES]!!).icon
                ),
                contentDescription = null,
            )
            Text(
                modifier = Modifier
                    .padding(start = 5.dp)
                    .constrainAs(name) {
                        start.linkTo(image.end)
                        top.linkTo(parent.top)
                        end.linkTo(endView.start)
                        width = Dimension.fillToConstraints
                    },
                text = media.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                modifier = Modifier
                    .padding(top = 3.dp, start = 5.dp)
                    .constrainAs(date) {
                        start.linkTo(image.end)
                        top.linkTo(name.bottom)
                        end.linkTo(endView.start)
                        width = Dimension.fillToConstraints
                    },
                text = SimpleDateFormat("dd/MM/yyyy").format(media.date),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
            Column(
                modifier = Modifier.constrainAs(endView) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
                verticalArrangement = Arrangement.Center
            ) {
                if (isShowChecked) ViewChecked(isSelected = media.isSelected) {
                    onClick(media)
                }
            }
        }
    }
}


@Composable
fun HighlightedBackground(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    content: @Composable () -> Unit
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        content()
        if (isSelected) Box(
            modifier = Modifier
                .aspectRatio(1f)
                .background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = Color.Blue
            )
        }
    }
}

@Preview(name = "ItemChildFolder")
@Composable
private fun PreviewItemChildFolder() {

}