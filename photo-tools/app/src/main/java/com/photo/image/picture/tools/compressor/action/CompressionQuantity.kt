package com.photo.image.picture.tools.compressor.action

import com.photo.image.picture.tools.compressor.domain.model.Resolution

sealed class CompressionQuantity(open var quantityDefault: Int) {
    data class VerySmallSize(override var quantityDefault: Int = 15) :
        CompressionQuantity(quantityDefault)

    data class SmallSize(override var quantityDefault: Int = 25) :
        CompressionQuantity(quantityDefault)

    data class MediumSize(override var quantityDefault: Int = 50) :
        CompressionQuantity(quantityDefault)

    data class LagerQuality(override var quantityDefault: Int = 75) :
        CompressionQuantity(quantityDefault)

    data class BestQuality(override var quantityDefault: Int = 100) :
        CompressionQuantity(quantityDefault)

    data class Custom(
        var size: Long = 0,
         override var quantityDefault: Int = 100,
        var resolutionCustom: Resolution? = null,
        var percent: Float = 100f
    ) : CompressionQuantity(quantityDefault) {

    }
}
