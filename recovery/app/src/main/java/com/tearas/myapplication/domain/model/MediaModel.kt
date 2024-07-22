package com.tearas.myapplication.domain.model

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class MediaModel(
    val id: String = UUID.randomUUID().toString(),
    val folder: FolderModel = FolderModel(),
    val name: String = "",
    val mime: String = "",
    val path: String = "",
    val time: Long = -1,
    val size: Long = 0,
    val date: Long = 0,
    val typeMedia: MediaType = MediaType.IMAGE,
    val contentHash: Int = -1,
    var isSelected: Boolean = false,
    val appIcon: Drawable? = null
)

enum class MediaType {
    VIDEO, AUDIO, FILES, IMAGE, ALL, JUNK_FILES, APK, FILE_LARGE, TEMPORARY_FILES
}
