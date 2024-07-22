package com.photo.image.picture.tools.compressor.domain.repository

import com.photo.image.picture.tools.compressor.domain.model.Image
import com.photo.image.picture.tools.compressor.domain.model.ImageType
import com.photo.image.picture.tools.compressor.domain.model.OptionImage

interface CompressRepository {
    fun compressPhoto(
        imageOptionImage: OptionImage, data: List<Image>
    )

    fun advanceCompress(
        imageOptionImage: OptionImage, data: List<Image>
    )

    fun resizeCompress(
        imageOptionImage: OptionImage, data: List<Image>
    )

    fun cropCompress(
        imageOptionImage: OptionImage, data: List<Image>
    )

    fun convertPhoto(
        formatTo: String = ImageType.JPEG.toString(), data: List<Image>
    )
}