package com.photo.image.picture.tools.compressor.utils

import android.util.Log
 import com.photo.image.picture.tools.compressor.BuildConfig

object XLog {
    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("XLog", msg)
        }
    }

    fun e(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e("XLog", throwable.stackTraceToString())
        }
    }
}