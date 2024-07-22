package com.tearas.myapplication.presentation.scan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tearas.myapplication.domain.model.OptionEnum
import com.tearas.myapplication.usecase.media.MediaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val mediaUseCases: MediaUseCases) :
    ViewModel() {
    var state by mutableStateOf(ScanImageState())
        private set


    fun onAction(action: ScanAction) {
        when (action) {
            is ScanAction.ScanImage -> scannerImage()
            is ScanAction.ScanAudio -> scannerAudio()
            is ScanAction.ScanVideo -> scannerVideo()
            is ScanAction.ScanDocument -> scannerDocument()
        }
    }

    fun handleScan(optionEnum: OptionEnum) {
        when (optionEnum) {
            OptionEnum.SCANNER_IMAGE -> onAction(ScanAction.ScanImage)
            OptionEnum.SCANNER_VIDEO -> onAction(ScanAction.ScanVideo)
            OptionEnum.SCANNER_AUDIO -> onAction(ScanAction.ScanAudio)
            OptionEnum.SCANNER_DOCUMENT -> onAction(ScanAction.ScanDocument)
            OptionEnum.SWEEP_FILES -> {}
            OptionEnum.DUPLICATE_FILES -> {}
        }
    }


    private fun scannerAudio() {
        state = state.copy(isSuccess = false, mapImages = emptyMap())
        viewModelScope.launch(Dispatchers.IO) {
            val scannerImage = mediaUseCases.scanAllAudio {
                state = state.copy(onPathFile = it.path)
                //      delay(10)
            }
            state = state.copy(mapImages = scannerImage, isSuccess = true)
        }
    }

    private fun scannerVideo() {
        state = state.copy(isSuccess = false, mapImages = emptyMap())
        viewModelScope.launch(Dispatchers.IO) {
            val scannerImage = mediaUseCases.scanAllIVideo {
                state = state.copy(onPathFile = it.path)
                //      delay(10)
            }
            state = state.copy(mapImages = scannerImage, isSuccess = true)
        }
    }

    private fun scannerDocument() {
        state = state.copy(isSuccess = false, mapImages = emptyMap())
        viewModelScope.launch(Dispatchers.IO) {
            val scannerImage = mediaUseCases.scanAllDocument {
                state = state.copy(onPathFile = it.path)
                //      delay(10)
            }
            state = state.copy(mapImages = scannerImage, isSuccess = true)
        }
    }

    private fun scannerImage() {
        state = state.copy(isSuccess = false, mapImages = emptyMap())
        viewModelScope.launch(Dispatchers.IO) {
            val scannerImage = mediaUseCases.scanAllImage {
                state = state.copy(onPathFile = it.path)
                //      delay(10)
            }
            state = state.copy(mapImages = scannerImage, isSuccess = true)
        }
    }


}