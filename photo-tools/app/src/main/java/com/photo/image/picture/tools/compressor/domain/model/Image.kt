package com.photo.image.picture.tools.compressor.domain.model

import android.graphics.Bitmap

data class Image(
    val id: Int = -1,
    val name: String,
    val path: String,
    var resolution: Resolution,
    val size: Long,
    var mime: ImageType,
    val date: Long,
    var bitmap: Bitmap? = null,
    var isSelected: Boolean = false
) {

}

