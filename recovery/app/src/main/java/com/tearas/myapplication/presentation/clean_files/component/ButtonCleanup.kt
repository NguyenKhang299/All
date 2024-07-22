package com.tearas.myapplication.presentation.clean_files.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tearas.myapplication.presentation.common.ButtonGradient
import com.tearas.myapplication.presentation.common.backgroundGradient

@Composable
fun ButtonCleanup(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    enabled: Boolean = false,
    onClick: () -> Unit
) {
    ButtonGradient(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isSelected = isSelected,
        onClick = { onClick() }
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium, color = Color.White)
    }

}

@Preview(name = "ButtonCleanup")
@Composable
private fun PreviewButtonRecovery() {

}