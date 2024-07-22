package com.tearas.myapplication.presentation.clean_files

import android.text.format.Formatter
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.tearas.myapplication.R
import com.tearas.myapplication.domain.model.StateScan
import com.tearas.myapplication.presentation.clean_files.component.ButtonCleanup
import com.tearas.myapplication.presentation.clean_files.component.FileCleanupView
import com.tearas.myapplication.presentation.common.AutoResizedText
import com.tearas.myapplication.presentation.common.BrushGradient
import com.tearas.myapplication.presentation.common.BrushGradientText
import com.tearas.myapplication.presentation.common.ButtonGradient
import com.tearas.myapplication.presentation.common.CardElevatedItem
import com.tearas.myapplication.presentation.common.DialogLoading
import com.tearas.myapplication.presentation.common.LoadingAnim
import com.tearas.myapplication.presentation.common.TopCenter
import com.tearas.myapplication.presentation.common.backgroundGradient
import com.tearas.myapplication.presentation.common.rotateInfinite
import com.tearas.myapplication.ui.theme.Background_Light
import com.tearas.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CleanFilesScreen(viewModel: CleanFilesViewModel, onBack: () -> Unit) {
    val state = viewModel.state
    val context = LocalContext.current

    var isPlay by remember { mutableStateOf(false) }
    var isShowDialogLoading by remember { mutableStateOf(false) }

    val animSize = animateFloatAsState(
        targetValue = if (isPlay) state.totalSize.toFloat() else 0f,
        label = "",
        animationSpec = tween(
            durationMillis = 500, easing = FastOutSlowInEasing
        )
    ).value

    LaunchedEffect(Unit) {
        isPlay = true
    }

    LaunchedEffect(Unit) {
        viewModel.onAction(CleanFilesAction.ScanFilesJunk)
    }
    if (isShowDialogLoading) {
        DialogLoading(
            angle = state.angle,
            title = stringResource(R.string.files_cleaning)
        ) { }
    }

    LaunchedEffect(key1 = state) {
        if (state.angle == 360f) {
            delay(1200)
            isShowDialogLoading = false
        }
    }


    LaunchedEffect(state.stateScan) {
        this.launch(Dispatchers.Main) {
            val message = when (state.stateScan) {
                StateScan.NOT_STARTED -> null
                StateScan.LOADING -> "Files Clean Loading..."
                StateScan.SUCCESS -> "Files Clean Successfully"
                StateScan.FAILED -> "Files Clean Failed"
            }
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Scaffold(topBar = {
        TopCenter(title = stringResource(id = R.string.clean_files)) {
            onBack()
        }
    }, bottomBar = {
        val isChecked = state.totalSizeMediaSelected > 0 && state.stateScan == StateScan.SUCCESS
        ButtonCleanup(modifier = Modifier
            .background(Color.White)
            .padding(15.dp),
            text = stringResource(id = R.string.clean_files) + " " + Formatter.formatFileSize(
                context, state.totalSizeMediaSelected
            ),
            enabled = isChecked,
            isSelected = isChecked,
            onClick = {
                viewModel.onAction(CleanFilesAction.DeleteFilesJunk)
                isShowDialogLoading = true
            })
    }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Cyan, Background_Light, Color.Cyan, Background_Light, Color.Cyan
                        )
                    )
                )
                .padding(it)
        ) {
            val (header, content) = createRefs()

            Column(modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    height = Dimension.percent(0.25f)
                }
                .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    LoadingAnim(
                        modifier = Modifier
                            .fillMaxHeight()
                            .rotateInfinite(), isPlaying = true
                    )
                    AutoResizedText(
                        modifier = Modifier.width((screenHeightDp * 0.25 * 0.5).dp),
                        text = Formatter.formatFileSize(context, animSize.toLong()),
                        style = MaterialTheme.typography.titleLarge.copy(
                            brush = BrushGradientText,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            fontFamily = FontFamily.Serif,
                            fontSize = 25.sp
                        ),
                        color = Color.White,
                    )
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10),
                modifier = Modifier
                    .constrainAs(content) {
                        bottom.linkTo(parent.bottom)
                        height = Dimension.percent(0.75f)
                    }
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStartPercent = 10,
                            topEndPercent = 10
                        )
                    )) {
                FileCleanupView(map = state.mapFilesClean,
                    onFolderChecked = { folderCleaningDTO, isChecked ->
                        viewModel.onAction(
                            CleanFilesAction.UpdateSelectedFolderMedia(
                                folderCleaningDTO, isChecked
                            )
                        )
                    },
                    onMediaClick = {
                        viewModel.onAction(CleanFilesAction.UpdateSelectedMedia(it))
                    })
            }
        }
    }
}

@Preview
@Composable
private fun CleanFilesScreenPre() {
    MyApplicationTheme {
        val viewModel: CleanFilesViewModel = hiltViewModel()
        CleanFilesScreen(viewModel) {}
    }
}