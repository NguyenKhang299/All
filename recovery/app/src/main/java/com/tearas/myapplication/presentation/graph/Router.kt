package com.tearas.myapplication.presentation.graph

sealed class Route(val route: String) {
    data object SplashScreen : Route("SplashScreen")
    data object HomeScreen : Route("HomeScreen")
    data object ScannerScreen : Route("ScannerScreen")
    data object ScannerAudioScreen : Route("ScannerAudioScreen")
    data object ScannerVideoScreen : Route("ScannerVideoScreen")
    data object ScannerDocumentScreen : Route("ScannerDocumentScreen")
    data object ScannerSweepFilesScreen : Route("ScannerSweepFilesScreen")
    data object ScannerDuplicateFilesScreen : Route("ScannerDuplicateFilesScreen")
    data object AllMediaScreen : Route("AllMediaScreen")
    data object FileRecoveryScreen : Route("FilesSavedScreen")
}