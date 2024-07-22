package com.tearas.myapplication.presentation.common

import android.util.Log
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EllipsizeTextView(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = style.color,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = 2
) {
    var displayText by remember { mutableStateOf(text) }
    var isOverflow by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = modifier
    ) {
        val maxWidth = maxWidth

        Text(
            text = displayText,
            style = style,
            color = color,
            textAlign = textAlign,
            maxLines = maxLines,
            onTextLayout = { result ->
                if ((result.lineCount > maxLines || result.didOverflowWidth)) {
                    val ellipsis = "..."
                    val targetLineCount = maxLines * 2
                    val textToFit = text.take((text.length / targetLineCount) * targetLineCount)
                    var truncatedText = textToFit
                    var tempText = "$truncatedText$ellipsis"

                    while (truncatedText.isNotEmpty() && (result.lineCount > maxLines || result.didOverflowWidth)) {
                        truncatedText = truncatedText.dropLast(1)
                        tempText = "$truncatedText$ellipsis"
                        displayText = tempText
                    }

                    isOverflow = true
                }
            },
            modifier = Modifier.width(maxWidth)
        )
    }
}


@Preview(name = "EllipsizeTextView")
@Composable
private fun PreviewEllipsizeTextView() {
    EllipsizeTextView(text = "AdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJAdADPSDJAISDJAOISDJIAOSJDOAIJSDOJ")
}