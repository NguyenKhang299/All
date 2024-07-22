package com.photo.imagecompressor.tools.action

import com.photo.imagecompressor.tools.domain.model.Resolution_


sealed class CompressionQuantity(open var quantityDefault: Int) {
    data class VerySmallSize(override var quantityDefault: Int = 20) :
        CompressionQuantity(quantityDefault)

    data class SmallSize(override var quantityDefault: Int = 40) :
        CompressionQuantity(quantityDefault)

    data class MediumSize(override var quantityDefault: Int = 60) :
        CompressionQuantity(quantityDefault)

    data class LagerQuality(override var quantityDefault: Int = 80) :
        CompressionQuantity(quantityDefault)

}
