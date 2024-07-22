package com.tearas.myapplication.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R

@Composable
fun ItemOption(
    modifier: Modifier = Modifier,
    title: String,
    idRs: Int,
    onClick: () -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp
    ElevatedCard(
        modifier = modifier.wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                 .clickable { onClick() }
                .aspectRatio(1.5f / 1f)
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = idRs),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Text(
                maxLines = 1,
                text = title,
                modifier = Modifier.padding(top = 10.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview(name = "ItemOption")
@Composable
private fun PreviewItemOption() {
    ItemOption(title = "Image scanner", idRs = R.drawable.image) {

    }
}