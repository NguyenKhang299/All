package com.photo.imagecompressor.tools.domain.model

class Resolution_(
    val rW: Int,
    val rH: Int
) {
    override fun toString(): String {
        return "$rW x $rH"
    }
}