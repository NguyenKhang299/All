package com.photo.imagecompressor.tools.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.net.toUri
import com.photo.imagecompressor.tools.domain.model.Image_Type
import java.io.File
import java.util.Locale


object FileUtils {
      fun getBitMapFromUri(context: Context, uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    uri
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
        }
    }

    fun exportImageToGallery(context: Context, filePath: String) {
        val file = File(filePath)
        MediaScannerConnection.scanFile(
            context, arrayOf(file.toString()),
            null, null
        )
    }

    fun getImageType(fileExtension: String): Image_Type {
        return when (fileExtension.lowercase(Locale.ROOT)) {
            "png" -> Image_Type.PNG
            "jpeg" -> Image_Type.JPEG
            "webp" -> Image_Type.WEBP
            else -> Image_Type.JPEG
        }
    }


}