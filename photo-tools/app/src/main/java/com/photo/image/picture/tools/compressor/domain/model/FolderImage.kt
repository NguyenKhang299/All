package com.photo.image.picture.tools.compressor.domain.model

import java.io.Serializable

class FolderImage(
    val id: Int,
    val name: String,
    val totalImage: Int = 0,
    val firstImage: String? = null
){
}