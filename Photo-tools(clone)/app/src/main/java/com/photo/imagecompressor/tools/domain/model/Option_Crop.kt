package com.photo.imagecompressor.tools.domain.model

class Option_Crop(val name: String, val x: Int, val y: Int, var isSelected: Boolean = false) {
    companion object {
        val optionCrops = listOf(
            Option_Crop("1:1", 1, 1, false),
            Option_Crop("3:4", 3, 4, false),
            Option_Crop("Original", 0, 0, true),
            Option_Crop("3:2", 3, 2, false),
            Option_Crop("16:9", 16, 9, false)
        )
    }
}