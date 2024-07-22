package com.image.effect.timewarp.scan.filtertiktok.face.filter.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import androidx.core.app.ActivityCompat

class Utils {
    companion object {
        fun pxToDp(px: Int): Int {
            return (px / Resources.getSystem().displayMetrics.density).toInt()
        }

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun screenHeight(): Int {
            return Resources.getSystem().displayMetrics.heightPixels
        }

        fun screenWidth(): Int {
            return Resources.getSystem().displayMetrics.widthPixels
        }

        fun isPackageInstalled(context: Context, packageName: String): Boolean {
            return try {
                context.packageManager.getApplicationInfo(packageName, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                false
            }
        }
    }
}