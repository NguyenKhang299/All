package com.tearas.myapplication.presentation.scan

import com.tearas.myapplication.domain.model.FolderModel
import com.tearas.myapplication.domain.model.MediaModel

data class ScanImageState(
    val onPathFile: String = "",
    val mapImages: Map<FolderModel, List<MediaModel>> = emptyMap(),
    val isSuccess: Boolean = false
)