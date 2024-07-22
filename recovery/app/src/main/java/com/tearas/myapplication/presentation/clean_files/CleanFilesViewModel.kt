package com.tearas.myapplication.presentation.clean_files

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.StateScan
import com.tearas.myapplication.domain.repository.IEventDeleteFiles
import com.tearas.myapplication.dto.FolderCleaningDTO
import com.tearas.myapplication.usecase.media.MediaUseCases
import com.tearas.myapplication.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CleanFilesViewModel @Inject constructor(
    private val mediaUseCases: MediaUseCases, @ApplicationContext private val context: Context
) : ViewModel() {
    var state by mutableStateOf(CleanFilesState())


    fun onAction(action: CleanFilesAction) {
        when (action) {
            is CleanFilesAction.ScanFilesJunk -> sweepFilesJunk()
            is CleanFilesAction.DeleteFilesJunk -> deleteFilesJunk()
            is CleanFilesAction.UpdateSelectedMedia -> updateSelectedMedia(action.media)
            is CleanFilesAction.UpdateSelectedFolderMedia -> updateSelectedFolderMedia(
                action.folderCleaningDTO, action.isSelected
            )
        }
    }

    private fun updateSelectedMedia(media: MediaModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val mediaSelected = media.copy(isSelected = !media.isSelected)
            val updatedMapFilesClean = state.mapFilesClean.mapValues { entry ->
                if (media in entry.value) entry.value.map { item ->
                    if (media == item) mediaSelected else item
                } else entry.value
            }.mapKeys { entry ->
                if (entry.value.contains(mediaSelected)) entry.key.copy(allSize = entry.value.sumOf { it.size }) else entry.key
            }
            state = state.copy(mapFilesClean = updatedMapFilesClean,
                totalSizeMediaSelected = updatedMapFilesClean.values.flatten()
                    .sumOf { if (it.isSelected) it.size else 0L })
        }
    }

    private fun updateSelectedFolderMedia(folderCleaningDTO: FolderCleaningDTO, selected: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedMapFilesClean = state.mapFilesClean.mapValues { entry ->
                if (folderCleaningDTO == entry.key) entry.value.map { item ->
                    item.copy(isSelected = selected)
                } else entry.value
            }
            state = state.copy(mapFilesClean = updatedMapFilesClean,
                totalSizeMediaSelected = updatedMapFilesClean.values.flatten()
                    .sumOf { if (it.isSelected) it.size else 0L })
        }
    }

    private fun deleteFilesJunk() {
        viewModelScope.launch(Dispatchers.IO) {
            val deleteFiles = state.mapFilesClean.values.flatten().filter { it.isSelected }
            val updatedMapFilesClean = state.mapFilesClean.mapValues {
                it.value.filter { !it.isSelected }
            }.mapKeys { entry ->
                entry.key.copy(allSize = entry.value.sumOf { it.size })
            }

            mediaUseCases.deleteMedia(list = deleteFiles, object : IEventDeleteFiles {
                override suspend fun onDelete(index: Int, mediaModel: MediaModel) {
                    delay(100)
                    state = state.copy(
                        angle = ((index + 1) / (deleteFiles.size).toFloat()) * 360
                    )
                }

                override suspend fun onFinish() {
                    delay(1300)
                    state = state.copy(
                        mapFilesClean = updatedMapFilesClean,
                        totalSizeMediaSelected = updatedMapFilesClean.values.flatten()
                            .sumOf { if (it.isSelected) it.size else 0L },
                        totalSize = updatedMapFilesClean.keys.sumOf { it.allSize },
                    )
                }

                override suspend fun onError(message: String) {

                }
            })
        }
    }

    private fun sweepFilesJunk() {
        viewModelScope.launch(Dispatchers.IO) {
            var map = Utils.createFolderCleaningMap(context)
            state = state.copy(
                mapFilesClean = map,
                totalSize = map.keys.sumOf { it.allSize },
                totalSizeMediaSelected = map.keys.sumOf { it.allSize },
                stateScan = StateScan.LOADING
            )
            mediaUseCases.sweepFilesJunk { mediaModel ->
                map = map.mapValues {
                    if (it.key.type == mediaModel.typeMedia) it.value.toMutableList()
                        .apply { add(mediaModel.apply { isSelected = true }) } else it.value
                }.mapKeys { entry ->
                    entry.key.copy(allSize = entry.value.sumOf { it.size })
                }
                state = state.copy(
                    mapFilesClean = map,
                    totalSize = map.keys.sumOf { it.allSize },
                    totalSizeMediaSelected = map.keys.sumOf { it.allSize },
                )
                delay(100)
            }
            state = state.copy(stateScan = StateScan.SUCCESS)
        }
    }
}