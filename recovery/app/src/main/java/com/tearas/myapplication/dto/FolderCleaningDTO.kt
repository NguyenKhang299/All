package com.tearas.myapplication.dto

import androidx.annotation.DrawableRes
import com.tearas.myapplication.domain.model.MediaType
import java.util.UUID

data class FolderCleaningDTO(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    @DrawableRes val icon: Int,
    var allSize: Long,
    val type: MediaType
)