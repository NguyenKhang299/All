package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.domain.model.OptionEnum

@Composable
fun AsyncImageVideo(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    media: MediaModel,
    mediaType: MediaType,
    onClick: ((MediaModel) -> Unit)? = null
) {
    val context = LocalContext.current

    AsyncImage(
        modifier = modifier
            .clickable(enabled = enabled) {
                if (onClick != null) {
                    onClick(media)
                }
            },
        model = if (mediaType == MediaType.IMAGE)
            media.path else ImageRequest.Builder(context)
            .data(media.path)
            .decoderFactory { result, options, _ ->
                VideoFrameDecoder(result.source, options)
            }
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}