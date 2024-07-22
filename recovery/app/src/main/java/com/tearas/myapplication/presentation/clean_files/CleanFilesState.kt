package com.tearas.myapplication.presentation.clean_files

import com.tearas.myapplication.domain.model.MediaModel
import com.tearas.myapplication.domain.model.StateScan
import com.tearas.myapplication.dto.FolderCleaningDTO

data class CleanFilesState(
    val mapFilesClean: Map<FolderCleaningDTO, List<MediaModel>> = emptyMap(),
    val totalSize: Long = 0,
    val totalSizeMediaSelected: Long = 0,
    val stateScan: StateScan = StateScan.NOT_STARTED,
    val angle: Float = 0F
)