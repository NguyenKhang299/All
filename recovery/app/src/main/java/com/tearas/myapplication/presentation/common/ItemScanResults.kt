package com.tearas.myapplication.presentation.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tearas.myapplication.R
import com.tearas.myapplication.ui.theme.Blue_Light

@Composable
fun ItemScanResults(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes icon: Int,
    filesSize: Int,
    body: String,
    description: String,
    onScanBack: () -> Unit
) {
    CardElevatedItem(modifier.fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                SpaceHorizontal()
                Text(text = title, style = MaterialTheme.typography.titleMedium)
            }
            SpaceVertical()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "$body: ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "$filesSize",
                    style = TextStyle(
                        brush = BrushGradient,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            SpaceVertical()
            ButtonGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .backgroundGradient(),
                onClick = { onScanBack() },
                enabled = true
            ) {
                Text(
                    text = stringResource(id = R.string.re_scan),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                SpaceHorizontal(width = 5.dp)
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.rotateInfinite()
                )
            }
            SpaceVertical(height = 5.dp)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = description,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(name = "ItemScanResults")
@Composable
private fun PreviewItemScanResults() {
    ItemScanResults(
        title = "Khôi phục ảnh",
        icon = R.drawable.image,
        filesSize = 1500,
        body = "Hình ảnh đã được tìm thấy",
        description = "Nhấp vào để tực hiện quét những ảnh mới",
    ) {

    }
}