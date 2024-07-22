package com.tearas.myapplication.presentation.recovery_save.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.dto.FolderMediaDTO
import com.tearas.myapplication.presentation.common.SpaceHorizontal

@Composable
fun ItemFolderMedia(
    folderMediaDTO: FolderMediaDTO,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .background(Color.White)
        .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = folderMediaDTO.icon),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            SpaceHorizontal()
            Text(text = folderMediaDTO.title, style = MaterialTheme.typography.titleMedium)
        }
        Text(text = "${folderMediaDTO.count}", style = MaterialTheme.typography.titleMedium)
    }
}