package com.tearas.myapplication.presentation.show_media

import com.tearas.myapplication.domain.model.FolderMediaMap
import com.tearas.myapplication.domain.model.MediaModel

data class ShowAllMediaState(
    val listMedia: List<MediaModel> = emptyList(),
    val countItemSelected: Int = 0,
    val angle: Float = 0f
)