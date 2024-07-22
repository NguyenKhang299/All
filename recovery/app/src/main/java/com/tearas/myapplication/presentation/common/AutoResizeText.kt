package com.tearas.myapplication.presentation.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AutoResizedText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = style.color,
    textAlign: TextAlign = TextAlign.Center,
 ) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }

    Text(modifier = modifier,
        textAlign = textAlign,
        text = text,
        color = color,
        softWrap = false,
        maxLines = 1,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.9
                )
            }
        }
    )
}