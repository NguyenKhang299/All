package com.photo.image.picture.tools.compressor.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.photo.image.picture.tools.compressor.domain.model.ImageType
import java.io.File
import java.io.FileOutputStream
import java.util.Locale


object FileUtils {
    fun exportImageToGallery(context: Context, filePath: String) {
        val file = File(filePath)
        MediaScannerConnection.scanFile(
            context, arrayOf(file.toString()),
            null, null
        )
    }

    fun getImageType(fileExtension: String): ImageType {
        return when (fileExtension.lowercase(Locale.ROOT)) {
            "png" -> ImageType.PNG
            "jpeg" -> ImageType.JPEG
            "webp" -> ImageType.WEBP
            else -> ImageType.JPEG
        }
    }


}