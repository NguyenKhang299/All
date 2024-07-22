package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R

@Composable
fun ButtonGradient(
    modifier: Modifier = Modifier,
    isSelected: Boolean = true,
    enabled: Boolean = false,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val buttonModifier = if (!isSelected) {
        modifier
            .background(color = Color.LightGray, shape = CircleShape)
    } else {
        modifier
            .backgroundGradient()
    }
    Row(
        modifier = buttonModifier
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                interactionSource = null,
                indication = rememberRipple(color = Color.Transparent)
            ) { onClick() }
            .padding(horizontal = 15.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}