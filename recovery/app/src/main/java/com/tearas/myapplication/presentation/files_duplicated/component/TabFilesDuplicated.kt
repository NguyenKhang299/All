package com.tearas.myapplication.presentation.files_duplicated.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.TabItem
import com.tearas.myapplication.presentation.common.SpaceHorizontal

val tabItems = listOf(
    TabItem(0, R.string.all, -1),
    TabItem(1, R.string.images, R.drawable.image),
    TabItem(2, R.string.videos, R.drawable.video),
    TabItem(3, R.string.audios, R.drawable.audio),
    TabItem(4, R.string.documents, R.drawable.document),
    TabItem(5, R.string.apk, R.drawable.android)
)

@Composable
fun TabFilesDuplicated(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int = 0,
    tabItems: List<TabItem>,
    onTabClick: (Int) -> Unit
) {

    ScrollableTabRow(
        modifier = modifier
            .fillMaxWidth(),
        containerColor = Color.White,
        edgePadding = 0.dp,
        selectedTabIndex = selectedTabIndex
    ) {
        tabItems.forEach { tabItem ->
            Tab(
                selected = tabItem.id == selectedTabIndex,
                onClick = { onTabClick(tabItem.id) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(15.dp)
                ) {
                    if (tabItem.icon != -1) {
                        Image(
                            painter = painterResource(id = tabItem.icon),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        SpaceHorizontal(width = 10.dp)
                    }
                    Text(text = stringResource(id = tabItem.name))
                }
            }
        }
    }
}


@Preview(name = "TabFilesDuplicated")
@Composable
private fun PreviewTabFilesDuplicated() {

}