package com.photo.image.picture.tools.compressor.domain.model

import com.photo.image.picture.tools.compressor.action.ActionImage
import com.photo.image.picture.tools.compressor.action.CompressionQuantity


data class OptionImage(
    var actionImage: ActionImage = ActionImage.COMPRESS_PHOTO,
    var compressionQuantity: CompressionQuantity = CompressionQuantity.MediumSize(),
    var formatTo: ImageType = ImageType.AUTO,
) {

}