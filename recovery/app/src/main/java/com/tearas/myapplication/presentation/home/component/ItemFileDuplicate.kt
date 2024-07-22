package com.tearas.myapplication.presentation.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstraintLayout

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import com.tearas.myapplication.R
import com.tearas.myapplication.presentation.common.CardElevatedItem

@Composable
fun ItemFileDuplicate(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CardElevatedItem(modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 10.dp, vertical = 15.dp)
        ) {
            val (thumb, des, endIcon) = createRefs()
            Image(
                modifier = Modifier
                    .constrainAs(thumb) {
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                    }
                    .size(40.dp),
                painter = painterResource(id = R.drawable.duplicated),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .constrainAs(des) {
                        start.linkTo(thumb.end)
                        end.linkTo(endIcon.start)
                        centerVerticallyTo(parent)
                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 15.dp),
                verticalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(id = R.string.duplicate_file_scan),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(id = R.string.description_duplicate),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(modifier = Modifier.constrainAs(endIcon) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)
            }, imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)

        }
    }
}

@Preview(name = "ItemClearFille")
@Composable
private fun PreviewItemClearFille() {
    ItemFileDuplicate {

    }
}