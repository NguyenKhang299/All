package com.tearas.myapplication.presentation.scan

sealed class ScanAction {
    data object ScanImage : ScanAction()
    data object ScanVideo : ScanAction()
    data object ScanAudio : ScanAction()
    data object ScanDocument : ScanAction()
}