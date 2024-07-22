package com.tearas.myapplication.presentation.home.component

import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.text.format.Formatter
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.constraintlayout.compose.ConstraintLayout

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import com.tearas.myapplication.R
import com.tearas.myapplication.presentation.common.AutoResizedText
import com.tearas.myapplication.presentation.common.BrushGradient
import com.tearas.myapplication.presentation.common.ButtonGradient
import com.tearas.myapplication.presentation.common.CardElevatedItem
import com.tearas.myapplication.presentation.common.SpaceHorizontal
import com.tearas.myapplication.presentation.common.SpaceVertical
import com.tearas.myapplication.presentation.common.backgroundGradient
import com.tearas.myapplication.presentation.common.rotateInfinite
import com.tearas.myapplication.ui.theme.Blue_Light
import com.tearas.myapplication.utils.StatUtils

@Composable
fun ItemClearFile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    CardElevatedItem(modifier) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable() { onClick() }
                .padding(10.dp),
        ) {
            val (content, pie) = createRefs()
            Column(
                modifier = Modifier.constrainAs(content) {
                    start.linkTo(parent.start)
                    end.linkTo(pie.start)
                    centerVerticallyTo(parent)
                    width = Dimension.fillToConstraints
                },
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Box {
                        Box(
                            modifier = Modifier
                                .background(BrushGradient, CircleShape)
                                .size(50.dp)
                                .rotateInfinite(),
                        )
                        Image(
                            modifier = Modifier
                                .size(50.dp),
                            painter = painterResource(id = R.drawable.sweep),
                            contentDescription = null
                        )
                    }
                    SpaceHorizontal()
                    Text(
                        text = stringResource(id = R.string.unnecessary_file_clea),
                        style = TextStyle(
                            brush = BrushGradient,
                            fontSize = 18.sp,
                        )
                    )
                }
                SpaceVertical()
                ButtonGradient(onClick = { }) {
                    Text(text = stringResource(id = R.string.clean_now), color = Color.White)
                    SpaceHorizontal(width = 10.dp)
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            val context = LocalContext.current
            val totalMemory = StatUtils.totalMemory(context)
            val angle = 360f * (StatUtils.busyMemory(context)) / totalMemory

            PieChart(
                modifier = Modifier.constrainAs(pie) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)
                }, angle = angle, chartBarWidth = 15.dp
            ) {
                AutoResizedText(
                    text = Formatter.formatFileSize(context, StatUtils.busyMemory(context)),
                    style = MaterialTheme.typography.titleSmall
                )
                AutoResizedText(
                    text = Formatter.formatFileSize(context, totalMemory),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview(name = "ItemClearFille")
@Composable
private fun PreviewItemClearFille() {
    ItemClearFile {

    }
}