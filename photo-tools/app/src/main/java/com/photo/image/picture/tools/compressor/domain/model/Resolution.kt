package com.photo.image.picture.tools.compressor.domain.model

class Resolution(
    val rW: Int,
    val rH: Int
) {
    override fun toString(): String {
        return "$rW x $rH"
    }
}