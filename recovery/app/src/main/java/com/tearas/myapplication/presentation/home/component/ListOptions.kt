package com.tearas.myapplication.presentation.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.domain.model.OptionModel
import com.tearas.myapplication.presentation.common.SpaceHorizontal
import com.tearas.myapplication.presentation.common.SpaceVertical

val options = listOf(
    OptionModel(
        title = R.string.scan_image,
        icon = R.drawable.image,
        type = OptionEnum.SCANNER_IMAGE
    ),
    OptionModel(
        title = R.string.scan_video,
        icon = R.drawable.video,
        type = OptionEnum.SCANNER_VIDEO
    ),
    OptionModel(
        title = R.string.scan_audio,
        icon = R.drawable.audio,
        type = OptionEnum.SCANNER_AUDIO
    ),
    OptionModel(
        title = R.string.scan_document,
        icon = R.drawable.document,
        type = OptionEnum.SCANNER_DOCUMENT
    ),
)

@Composable
fun ListOptions(
    modifier: Modifier = Modifier,
    onClickOption: (OptionModel) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(15.dp)
    ) {
        options.forEach { option ->
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        ItemOption(
                            title = stringResource(id = option.title),
                            idRs = option.icon,
                        ) { onClickOption(option) }
                    }
                }
            }
        }
    }
}

@Preview(name = "ListOptions")
@Composable
private fun PreviewListOptions() {
    ListOptions {}
}