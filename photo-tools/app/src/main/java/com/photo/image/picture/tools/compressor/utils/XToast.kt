package com.photo.image.picture.tools.compressor.utils

import android.content.Context
import android.widget.Toast

object XToast {
    fun show(context: Context, text: CharSequence) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}