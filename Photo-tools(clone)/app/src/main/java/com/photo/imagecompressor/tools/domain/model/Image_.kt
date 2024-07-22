package com.photo.imagecompressor.tools.domain.model

import android.graphics.Bitmap

data class Image_(
    val id: Int = -1,
    val name: String,
    val path: String,
    var resolution: Resolution_,
    val size: Long,
    var mime: Image_Type,
    val date: Long,
    var bitmap: Bitmap? = null,
    var isSelected: Boolean = false
) {
    override fun toString(): String {
        return "Image_(id=$id, name='$name', path='$path', resolution=$resolution, size=$size, mime=$mime, date=$date, bitmap=$bitmap, isSelected=$isSelected)"
    }
}

