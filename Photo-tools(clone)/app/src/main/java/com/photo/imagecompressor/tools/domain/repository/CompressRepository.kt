package com.photo.imagecompressor.tools.domain.repository

import com.photo.imagecompressor.tools.domain.model.Image_
import com.photo.imagecompressor.tools.domain.model.Image_Type
import com.photo.imagecompressor.tools.domain.model.Option_Image

interface CompressRepository {
    fun compressPhoto(
        imageOptionImage: Option_Image, data: List<Image_>
    )

    fun advanceCompress(
        imageOptionImage: Option_Image, data: List<Image_>
    )

    fun resizeCompress(
        imageOptionImage: Option_Image, data: List<Image_>
    )

    fun cropCompress(
        imageOptionImage: Option_Image, data: List<Image_>
    )

    fun convertPhoto(
        formatTo: String = Image_Type.JPEG.toString(), data: List<Image_>
    )
}