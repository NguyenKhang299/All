package com.photo.image.picture.tools.compressor.utils

import android.content.Intent
import android.os.Build
import com.photo.image.picture.tools.compressor.action.ActionImage

fun Intent.passActionImage(actionImage: ActionImage) {
    putExtra("action_image", actionImage)
}

fun Intent.getActionImage() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    getSerializableExtra("action_image", ActionImage::class.java)
} else {
    getSerializableExtra("action_image") as ActionImage
}
