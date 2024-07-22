package com.tearas.myapplication.presentation.recovery_save

import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.dto.FolderMediaDTO

data class FilesSavedState(
    val mapFolder: Map<FolderMediaDTO, List<MediaModel>> = emptyMap(),
    val isSuccess: Boolean? = null,
    val angle: Float = 0f,
    val countMediaSelected: Int = 0
)