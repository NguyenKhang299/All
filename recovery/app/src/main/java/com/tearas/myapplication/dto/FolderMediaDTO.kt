package com.tearas.myapplication.dto

import androidx.annotation.DrawableRes
import com.tearas.myapplication.domain.model.MediaType

data class FolderMediaDTO(
    val title: String, @DrawableRes val icon: Int, var count: Int, val type: MediaType
)