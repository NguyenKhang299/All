package com.tearas.myapplication.presentation.home

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.presentation.common.CardElevatedItem
import com.tearas.myapplication.presentation.common.PermissionExplanationDialogHigherSdk30
import com.tearas.myapplication.presentation.common.PermissionExplanationDialogLowerSdk30
import com.tearas.myapplication.presentation.common.SpaceVertical
import com.tearas.myapplication.presentation.graph.Route
import com.tearas.myapplication.presentation.home.component.ItemClearFile
import com.tearas.myapplication.presentation.home.component.ItemFileDuplicate
import com.tearas.myapplication.presentation.home.component.ListOptions
import com.tearas.myapplication.presentation.home.component.TopBarHome
import com.tearas.myapplication.ui.theme.MyApplicationTheme
import com.tearas.myapplication.utils.isAllManager
import com.tearas.myapplication.utils.permissionHigherSdk30
import com.tearas.myapplication.utils.permissionLowerSdk30
import java.io.File

private val mapDestination = mapOf(
    OptionEnum.SCANNER_VIDEO to Route.ScannerScreen.route + "/${OptionEnum.SCANNER_VIDEO}",
    OptionEnum.SCANNER_AUDIO to Route.ScannerScreen.route + "/${OptionEnum.SCANNER_AUDIO}",
    OptionEnum.SCANNER_IMAGE to Route.ScannerScreen.route + "/${OptionEnum.SCANNER_IMAGE}",
    OptionEnum.SCANNER_DOCUMENT to Route.ScannerScreen.route + "/${OptionEnum.SCANNER_DOCUMENT}",
    OptionEnum.SWEEP_FILES to Route.ScannerSweepFilesScreen.route,
    OptionEnum.DUPLICATE_FILES to Route.ScannerDuplicateFilesScreen.route,
)

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onDestination: (String) -> Unit) {
    val context = LocalContext.current
    var isGrant by remember { mutableStateOf(false) }
    var optionClick by remember { mutableStateOf<OptionEnum?>(null) }
    var isShowDialog by remember { mutableStateOf(false) }

    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isGrant = permissions.values.all { it }
        optionClick = null
    }

    val startActivityForResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        isGrant = isAllManager(context)
        optionClick = null
    }

    if (isShowDialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            PermissionExplanationDialogHigherSdk30(
                onDismiss = {
                    isShowDialog = false
                    optionClick = null
                },
                onConfirm = {
                    isShowDialog = false
                    startActivityForResult.launch(permissionHigherSdk30(context))
                }
            )
        } else {
            PermissionExplanationDialogLowerSdk30(
                onDismiss = {
                    isShowDialog = false
                    optionClick = null
                },
                onConfirm = {
                    isShowDialog = false
                    requestPermission.launch(permissionLowerSdk30())
                }
            )
        }
    }

    LaunchedEffect(optionClick, isGrant) {
        when {
            isAllManager(context) -> {
                optionClick?.let { option ->
                    mapDestination[option]?.let(onDestination)
                }
            }

            optionClick != null -> {
                isShowDialog = true
            }
        }
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = { TopBarHome() }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFebeef5))
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SpaceVertical()
            ItemClearFile { optionClick = OptionEnum.SWEEP_FILES }
            ListOptions { optionClick = it.type }
            ItemFileDuplicate { optionClick = OptionEnum.DUPLICATE_FILES }
            SpaceVertical(height = 50.dp)
            CardElevatedItem(modifier = Modifier.padding(10.dp)) {
                Box(modifier = Modifier
                    .clickable { onDestination(Route.FileRecoveryScreen.route) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(40.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "HomeScreen", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewHomeScreen() {
    MyApplicationTheme {
        HomeScreen() {}
    }
}