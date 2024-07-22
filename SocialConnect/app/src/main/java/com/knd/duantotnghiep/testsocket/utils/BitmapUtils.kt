package com.knd.duantotnghiep.testsocket.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat

object BitmapUtils {
    fun getBitmap(context: Context, drawableId: Int, width: Int, height: Int): Bitmap? {
        if (width <= 0 || height <= 0) return null
        var bitmap: Bitmap? = null

        val drawable = ContextCompat.getDrawable(context, drawableId)!!


        bitmap = Bitmap.createBitmap(
            width,
            height, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width,   height)
        drawable.draw(canvas)


        return bitmap
    }
}