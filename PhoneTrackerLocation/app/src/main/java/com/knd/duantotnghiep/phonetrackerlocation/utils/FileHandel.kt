package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object FileHandel {
    fun ImageView.saveImage(context: Context): File? {
        return try {
            val bitmap = getBitmap(this)
            val file = File(context.filesDir, System.currentTimeMillis().toString() + ".png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            null
        }

    }

      fun getUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(context, Constants.AUTHORITY, file)
    }

    private fun getBitmap(imageView: ImageView): Bitmap {
        return imageView.let { (it.drawable as BitmapDrawable).bitmap }
    }
}