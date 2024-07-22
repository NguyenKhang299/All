package com.tearas.myapplication.presentation.graph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.presentation.clean_files.CleanFilesScreen
import com.tearas.myapplication.presentation.clean_files.CleanFilesViewModel
import com.tearas.myapplication.presentation.files_duplicated.FilesDuplicateViewModel
import com.tearas.myapplication.presentation.files_duplicated.FilesDuplicatedScreen
import com.tearas.myapplication.presentation.home.HomeScreen
import com.tearas.myapplication.presentation.recovery_save.FilesSavedScreen
import com.tearas.myapplication.presentation.recovery_save.FilesSavedViewModel
import com.tearas.myapplication.presentation.scan.ScanScreen
import com.tearas.myapplication.presentation.scan.ScanViewModel
import com.tearas.myapplication.presentation.show_media.ShowAllMediaScreen
import com.tearas.myapplication.presentation.show_media.ShowAllMediaViewModel
import com.tearas.myapplication.presentation.splash.SplashScreen
import com.tearas.myapplication.utils.getObject
import com.tearas.myapplication.utils.setObject

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen.route,
        exitTransition = { slideOutHorizontally(animationSpec = tween(durationMillis = 300)) + fadeOut() }) {
        composable(route = Route.SplashScreen.route) {
            SplashScreen {
                navController.navigate(it)
            }
        }
        composable(route = Route.HomeScreen.route) {
            HomeScreen {
                navController.navigate(it)
            }
        }
        composable(route = Route.ScannerSweepFilesScreen.route) {
            val viewModel: CleanFilesViewModel = hiltViewModel()
            CleanFilesScreen(viewModel) { navController.popBackStack() }
        }
        composable(
            route = Route.ScannerScreen.route + "/{option}",
            arguments = listOf(
                navArgument("option") {
                    type = NavType.StringType
                }
            )
        ) { nav ->
            val option = nav.arguments?.getString("option")!!
            val viewModel: ScanViewModel = hiltViewModel()

            ScanScreen(
                viewModel = viewModel,
                optionEnum = OptionEnum.valueOf(option),
                onDestination = { nameFolder, listMedia ->
                    navController.setObject("listMedia", listMedia)
                    navController.navigate(Route.AllMediaScreen.route + "/$nameFolder/true")
                },
                onBack = {
                    navController.popBackStack()
                })
        }


        composable(route = Route.ScannerDuplicateFilesScreen.route) {
            val viewModel: FilesDuplicateViewModel = hiltViewModel()
            FilesDuplicatedScreen(viewModel = viewModel) { navController.popBackStack() }
        }
        composable(
            route = Route.FileRecoveryScreen.route,
            enterTransition = { slideInHorizontally(animationSpec = tween(durationMillis = 300)) + fadeIn() }
        ) {
            val viewModel: FilesSavedViewModel = hiltViewModel()
            FilesSavedScreen(
                viewModel = viewModel,
                onDestination = { nameFolder, listMedia ->
                    navController.setObject("listMedia", listMedia)
                    navController.navigate(Route.AllMediaScreen.route + "/$nameFolder/false")
                })
            { navController.popBackStack() }
        }
        composable(
            route = Route.AllMediaScreen.route + "/{nameFolder}/{isRestore}",
            arguments = listOf(
                navArgument("nameFolder") {
                    type = NavType.StringType
                },
                navArgument("isRestore") {
                    type = NavType.BoolType
                }
            )
        ) { nav ->
            val viewModel: ShowAllMediaViewModel = hiltViewModel()
            val nameFolder = nav.arguments?.getString("nameFolder")
            val isRestore = nav.arguments?.getBoolean("isRestore") ?: true
            val listMedia = navController.getObject<List<MediaModel>>("listMedia")
            if (nameFolder != null && listMedia != null) ShowAllMediaScreen(
                isRestore = isRestore,
                nameFolder = nameFolder,
                listMedia = listMedia,
                viewModel = viewModel
            ) {
                navController.popBackStack()
            }
        }
    }

}