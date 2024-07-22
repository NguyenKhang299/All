package com.tearas.myapplication.presentation.common

import android.text.format.Formatter
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tearas.myapplication.presentation.home.component.PieChart
import com.tearas.myapplication.utils.StatUtils

@Composable
fun DialogLoading(angle: Float, title: String, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PieChart(
                angle = angle, chartBarWidth = 15.dp
            ) {
                AutoResizedText(
                    text = ((angle * 100) / 360f).toString() + " %",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
            }
            Text(text = title, fontStyle = FontStyle.Italic, color = Color.White)
        }
    }
}