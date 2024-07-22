package com.tearas.myapplication.presentation.show_media;

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.repository.IEventDeleteFiles
import com.tearas.myapplication.presentation.show_media.component.OptionsSort
import com.tearas.myapplication.usecase.media.MediaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowAllMediaViewModel @Inject constructor(private val mediaUseCases: MediaUseCases) :
    ViewModel() {
    var state by mutableStateOf(ShowAllMediaState())
        private set

    fun setListMedia(listMedia: List<MediaModel>) {
        state = state.copy(listMedia = listMedia)
    }

    fun onAction(event: ShowAllMediaAction) {
        when (event) {
            is ShowAllMediaAction.Delete -> deleteMedia()
            is ShowAllMediaAction.Recovery -> recoveryMedia()
            is ShowAllMediaAction.UpdateItemSelected -> updateItemSelected(event.mediaModel)
            is ShowAllMediaAction.SortMedia -> sortMedia(event.option)
        }
    }

    private fun deleteMedia() {
        state = state.copy(angle = 0F)
        viewModelScope.launch {
            val updatedList = state.listMedia.filter { !it.isSelected }

            val listMediaDelete = state.listMedia.filter { it.isSelected }
            delay(500)

            mediaUseCases.deleteMedia(list = listMediaDelete, object : IEventDeleteFiles {
                override suspend fun onDelete(index: Int, mediaModel: MediaModel) {
                    delay(100)
                    state = state.copy(
                        angle = ((index + 1) / (listMediaDelete.size).toFloat()) * 360
                    )
                }

                override suspend fun onFinish() {
                    delay(1300)
                    state = state.copy(listMedia = listMediaDelete)
                    state = state.copy(
                        listMedia = updatedList,
                        countItemSelected = updatedList.count { it.isSelected }
                    )
                }

                override suspend fun onError(message: String) {
                }
            })
        }
    }

    private fun sortMedia(option: OptionsSort) {
        val sortFolderMap = state.listMedia.let { listMedia ->
            val sortedMedia = when (option) {
                OptionsSort.BY_DATE -> listMedia.sortedByDescending { it.date }

                OptionsSort.BY_NAME -> listMedia.sortedBy { it.name }

                OptionsSort.BY_SIZE -> listMedia.sortedByDescending { it.size }
            }
            sortedMedia
        }.map { it }.toList()
        state = state.copy(listMedia = sortFolderMap)
    }


    private fun updateItemSelected(mediaModel: MediaModel) {
        viewModelScope.launch {
            val index = state.listMedia.indexOf(mediaModel)
            if (index >= 0) {
                val updatedMedia = state.listMedia.map {
                    if (mediaModel == it) mediaModel.copy(isSelected = !it.isSelected) else it
                }

                state = state.copy(
                    listMedia = updatedMedia,
                    countItemSelected = updatedMedia.count { it.isSelected }
                )
            }
        }
    }

    private fun recoveryMedia() {
        viewModelScope.launch(Dispatchers.IO) {
            state.listMedia.let { it ->
                delay(500)
                val listRecovery = it.filter { it.isSelected }
                mediaUseCases.recoveryMedia(listRecovery) { index ->
                    delay(100)
                    state =
                        state.copy(angle = ((index + 1) / (state.countItemSelected).toFloat()) * 360)
                }
                delay(1300)
                state = state.copy(
                    listMedia = state.listMedia.map { it.copy(isSelected = false) },
                    countItemSelected = 0,
                    angle = 0f
                )
            }
        }
    }
}