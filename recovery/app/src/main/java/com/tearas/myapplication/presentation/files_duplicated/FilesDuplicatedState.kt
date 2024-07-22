package com.tearas.myapplication.presentation.files_duplicated

import com.tearas.myapplication.domain.model.MediaType
import com.tearas.myapplication.domain.model.SetModel

data class FilesDuplicatedState(
    val mapSet: Map<MediaType, List<SetModel>> = emptyMap(),
    val isSuccess: Boolean = false,
    val onPathFile: String = "",
    val isStart: Boolean = false,
    val angle: Float = 0f,
    val countMediaSelected: Int = 0
)