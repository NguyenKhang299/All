package com.photo.imagecompressor.tools.utils

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermUtils {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    const val READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    const val READ_MEDIA_VISUAL_USER_SELECTED = Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
    const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true;
    }

    fun checkImageAccessPermissions(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                    ContextCompat.checkSelfPermission(
                        context,
                        READ_MEDIA_IMAGES
                    ) == PERMISSION_GRANTED -> true

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                    ContextCompat.checkSelfPermission(
                        context,
                        READ_MEDIA_VISUAL_USER_SELECTED
                    ) == PERMISSION_GRANTED -> true

            ContextCompat.checkSelfPermission(
                context,
                READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                WRITE_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED -> true

            else -> false
        }
    }

    fun getImageAccessPermissions() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(READ_MEDIA_IMAGES)
        } else {
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
        }
}