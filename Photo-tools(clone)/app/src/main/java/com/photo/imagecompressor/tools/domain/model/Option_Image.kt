package com.photo.imagecompressor.tools.domain.model


import com.photo.imagecompressor.tools.action.ActionImage
import com.photo.imagecompressor.tools.action.CompressionQuantity


data class Option_Image(
    var actionImage: ActionImage = ActionImage.COMPRESS_PHOTO,
    var compressionQuantity: CompressionQuantity = CompressionQuantity.MediumSize(),
    var formatTo: Image_Type = Image_Type.AUTO,
) {

}