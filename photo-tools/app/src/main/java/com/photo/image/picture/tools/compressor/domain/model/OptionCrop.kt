package com.photo.image.picture.tools.compressor.domain.model

import com.yalantis.ucrop.model.AspectRatio

class OptionCrop(val name: String, val x: Int, val y: Int, var isSelected: Boolean = false) {
    companion object {
        val optionCrops = listOf(
            OptionCrop("1:1", 1, 1, false),
            OptionCrop("3:4", 3, 4, false),
            OptionCrop("Original", 0, 0, true),
            OptionCrop("3:2", 3, 2, false),
            OptionCrop("16:9", 16, 9, false)
        )
    }
}