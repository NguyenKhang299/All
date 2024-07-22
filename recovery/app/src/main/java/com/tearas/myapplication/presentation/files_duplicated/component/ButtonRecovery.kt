package com.tearas.myapplication.presentation.files_duplicated.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tearas.myapplication.R
import com.tearas.myapplication.presentation.common.ButtonGradient
import com.tearas.myapplication.presentation.common.backgroundGradient

@Composable
fun ButtonRecovery(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean = false,
    enabled: Boolean = false,
    onClick: () -> Unit
) {
    Box(modifier) {
        ButtonGradient(
            enabled = enabled,
            modifier = if (!isSelected) Modifier
                .fillMaxWidth()
                .background(
                    color = Color.LightGray, shape = CircleShape
                ) else Modifier
                .fillMaxWidth()
                .backgroundGradient(),
            onClick = { onClick() }) {
            Text(
                color = Color.White,
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(name = "ButtonCleanup")
@Composable
private fun PreviewButtonRecovery() {
    ButtonRecovery(text = "") {}
}