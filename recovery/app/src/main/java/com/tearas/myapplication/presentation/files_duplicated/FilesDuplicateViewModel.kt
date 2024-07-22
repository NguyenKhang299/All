package com.tearas.myapplication.presentation.files_duplicated

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.repository.IEventDeleteFiles
import com.tearas.myapplication.usecase.media.MediaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesDuplicateViewModel @Inject constructor(private val useCases: MediaUseCases) :
    ViewModel() {
    var state by mutableStateOf(FilesDuplicatedState())
        private set

    fun onAction(action: FilesDuplicatedAction) {
        when (action) {
            is FilesDuplicatedAction.ScanFilesDuplicated -> scanFilesDuplicated()
            is FilesDuplicatedAction.UpdateMediaClick -> updateMediaClick(
                action.idSet, action.media
            )

            is FilesDuplicatedAction.UpdateSetClick -> updateSetClick(
                action.idSet, action.isSelected
            )

            is FilesDuplicatedAction.DeleteFile -> deleteMedia()
        }
    }

    private fun deleteMedia() {
        state = state.copy(angle = 0F)
        viewModelScope.launch {
            val updatedMapSet = state.mapSet.mapValues { entry ->
                entry.value.map { item ->
                    item.copy(listMedia = item.listMedia.filter { !it.isSelected })
                }
            }.filterValues { list ->
                list.any { it.listMedia.isNotEmpty() }
            }

            val listMediaDelete =
                state.mapSet.values.flatten().flatMap { it.listMedia }.filter { it.isSelected }
            delay(500)

            useCases.deleteMedia(list = listMediaDelete, object : IEventDeleteFiles {
                override suspend fun onDelete(index: Int, mediaModel: MediaModel) {
                    delay(100)
                    state = state.copy(
                        angle = ((index + 1) / (listMediaDelete.size).toFloat()) * 360
                    )
                }

                override suspend fun onFinish() {
                    delay(1300)
                    state = state.copy(mapSet = updatedMapSet)
                    updateMediaSelected()
                }

                override suspend fun onError(message: String) {
                }
            })
        }
    }

    private fun updateSetClick(idSet: String, selected: Boolean) {
        viewModelScope.launch {
            val updatedMapSet = state.mapSet.mapValues { entry ->
                entry.value.map { item ->
                    if (item.id == idSet) {
                        item.copy(listMedia = item.listMedia.map { it.copy(isSelected = selected) })
                    } else {
                        item
                    }
                }
            }
            state = state.copy(mapSet = updatedMapSet)
            updateMediaSelected()
        }
    }

    private fun updateMediaSelected() {
        val countMediaSelected =
            state.mapSet.values.flatten().flatMap { it.listMedia }.count { it.isSelected }
        state = state.copy(countMediaSelected = countMediaSelected)
    }

    private fun updateMediaClick(idSet: String, media: MediaModel) {
        viewModelScope.launch {
            val updatedMapSet = state.mapSet.mapValues { entry ->
                entry.value.map { item ->
                    if (item.id == idSet) {
                        item.copy(listMedia = item.listMedia.toMutableList().apply {
                            val index = this.indexOf(media)
                            this[index] = media.copy(isSelected = !media.isSelected)
                        })
                    } else {
                        item
                    }
                }
            }
            state = state.copy(mapSet = updatedMapSet)
            updateMediaSelected()
        }
    }


    private fun scanFilesDuplicated() {
        state = state.copy(isStart = true)
        viewModelScope.launch(Dispatchers.IO) {
            val mapSet = useCases.scanFilesDuplicated {
                state = state.copy(onPathFile = it.path)
            }
            state = state.copy(isStart = false, isSuccess = true, mapSet = mapSet)
        }
    }
}