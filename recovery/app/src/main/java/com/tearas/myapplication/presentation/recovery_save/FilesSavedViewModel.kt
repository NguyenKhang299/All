package com.tearas.myapplication.presentation.recovery_save

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.repository.IEventDeleteFiles
import com.tearas.myapplication.usecase.media.MediaUseCases
import com.tearas.myapplication.utils.Utils.calculateAngle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesSavedViewModel @Inject constructor(private val mediaUseCases: MediaUseCases) :
    ViewModel() {
    var state by mutableStateOf(FilesSavedState())
        private set

    init {

    }

    fun onAction(action: FilesSavedAction) {
        when (action) {
            is FilesSavedAction.GetFilesSaved -> getFilesRecoverSaved()
            is FilesSavedAction.DeleteFile -> deleteFiles()
            is FilesSavedAction.UpdateMediaSelected -> updateMediaSelected(action.mediaModel)
        }
    }

    private fun updateMediaSelected(media: MediaModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedMapSet = state.mapFolder.mapValues { entry ->
                entry.value.map { item ->
                    if (item == media)
                        item.copy(isSelected = !item.isSelected)
                    else item
                }
            }
            val countMediaSelected = updatedMapSet.values.flatten().count { it.isSelected }
            state = state.copy(mapFolder = updatedMapSet, countMediaSelected = countMediaSelected)
        }
    }

    private fun deleteFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(angle = 0F)

            val updatedMapSet = state.mapFolder.mapValues { entry ->
                entry.value.mapNotNull { item ->
                    if (!item.isSelected) item else null
                }
            }.mapKeys { entry ->
                entry.key.copy(count = entry.value.size)
            }

            val listMediaDelete = state.mapFolder.values.flatten().filter { it.isSelected }

            delay(500)

            mediaUseCases.deleteMedia(list = listMediaDelete, object : IEventDeleteFiles {
                override suspend fun onDelete(index: Int, mediaModel: MediaModel) {
                    delay(100)
                    state = state.copy(angle = calculateAngle(index, listMediaDelete.size))
                }

                override suspend fun onFinish() {

                }

                override suspend fun onError(message: String) {
                    delay(1300)
                    state = state.copy(mapFolder = updatedMapSet, countMediaSelected = 0)
                }
            })
        }
    }

    private fun getFilesRecoverSaved() {
        viewModelScope.launch(Dispatchers.IO) {
            val mapFolder = mediaUseCases.getFilesRecoverSaved()
            state = state.copy(mapFolder = mapFolder)
        }
    }
}