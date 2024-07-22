package com.tearas.myapplication.presentation.show_media.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.ItemMenu
import com.tearas.myapplication.presentation.common.BaseMenu

enum class OptionsSort {
    BY_NAME, BY_SIZE, BY_DATE
}

@Composable
fun MenuSort(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    onSort: (OptionsSort) -> Unit,
    onDismissRequest: () -> Unit
) {
    val heightScreen = LocalConfiguration.current.screenWidthDp

    val listMenu = remember {
        mutableStateListOf(
            ItemMenu(R.string.sort_by_name, R.drawable.desc_name, OptionsSort.BY_NAME),
            ItemMenu(R.string.sort_by_date, R.drawable.sort_date, OptionsSort.BY_DATE),
            ItemMenu(R.string.sort_by_size, R.drawable.sort_size, OptionsSort.BY_SIZE)
        )
    }

    BaseMenu(
        colorItemSelected = Color.Cyan.copy(alpha = 0.3f),
        onDismissRequest = onDismissRequest,
        isExpanded = isExpanded,
        listMenu = listMenu,
        offset = DpOffset(heightScreen.dp, 0.dp)
    ) { item ->
        if (item.option is OptionsSort) {
            listMenu.forEach {
                it.isSelected = item == it
            }
            onSort(item.option)
        }
    }
}
