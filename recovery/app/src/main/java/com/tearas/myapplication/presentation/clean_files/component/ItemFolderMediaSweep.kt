package com.tearas.myapplication.presentation.clean_files.component

import android.text.format.Formatter
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.dto.FolderCleaningDTO
import com.tearas.myapplication.presentation.common.SpaceHorizontal
import com.tearas.myapplication.presentation.common.ViewChecked

@Composable
fun ItemFolderMediaSweep(
    folderCleaningDTO: FolderCleaningDTO,
    isSelected: Boolean = true,
    onChecked: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var isPlay by remember { mutableStateOf(false) }
    val animSize = animateFloatAsState(
        targetValue = if (isPlay) folderCleaningDTO.allSize.toFloat() else 0f,
        label = "",
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    ).value

    LaunchedEffect(key1 = folderCleaningDTO) {
        isPlay = true
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .clickable { onClick() }
        .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = folderCleaningDTO.icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            SpaceHorizontal()
            Text(text = folderCleaningDTO.title, style = MaterialTheme.typography.titleMedium)
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = Formatter.formatFileSize(context, animSize.toLong()),
                style = MaterialTheme.typography.titleMedium
            )
            ViewChecked(isSelected = isSelected) {
                onChecked(!isSelected)
            }
        }
    }
}