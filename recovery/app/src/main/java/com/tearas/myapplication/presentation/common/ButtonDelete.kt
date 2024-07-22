package com.tearas.myapplication.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.ui.theme.MyApplicationTheme

@Composable
fun FloatingActionButtonDelete(
    modifier: Modifier = Modifier,
    description: String,
    onClick: () -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotateState = animateFloatAsState(
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearOutSlowInEasing
        ),
        targetValue = if (expandedState) 180f else 0f, label = ""
    ).value
    FloatingActionButton(
        modifier = modifier,
        onClick = { onClick() },
        containerColor = Color.Red
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
            Icon(
                modifier = Modifier
                    .rotate(rotateState)
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) { expandedState = !expandedState },
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                tint = Color.White
            )
            AnimatedVisibility(visible = !expandedState) {
                Text(text = stringResource(id = R.string.delete), color = Color.White)
            }
            AnimatedVisibility(visible = expandedState) {
                Text(
                    text = description,
                    color = Color.White,
                    fontStyle = FontStyle.Italic
                )
            }
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}


@Preview(name = "ButtonDelete")
@Composable
private fun PreviewButtonDelete() {
    MyApplicationTheme {
        FloatingActionButtonDelete(description = "") {}
    }
}