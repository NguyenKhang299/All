package com.tearas.myapplication.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.ItemMenu

@Composable
fun BaseMenu(
    modifier: Modifier = Modifier,
    listMenu: List<ItemMenu> = emptyList(),
    isExpanded: Boolean = false,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    colorItemDefault: Color = Color.White,
    colorItemSelected: Color = Color.White,
    onDismissRequest: () -> Unit,
    onMenuSelected: (ItemMenu) -> Unit
) {
    DropdownMenu(
        modifier = modifier.background(Color.White),
        offset = offset,
        expanded = isExpanded,
        onDismissRequest = onDismissRequest
    ) {
        listMenu.forEach { itemMenu ->
            DropdownMenuItem(
                modifier = Modifier.background(if (itemMenu.isSelected) colorItemSelected else colorItemDefault),
                text = { BasicText(text = stringResource(id = itemMenu.text)) },
                onClick = { onMenuSelected(itemMenu) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = itemMenu.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = MenuDefaults.itemColors(

                )
            )
        }
    }
}

@Preview(name = "BaseMenu")
@Composable
private fun PreviewBaseMenu() {

}