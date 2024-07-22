package com.photo.imagecompressor.tools.utils

import android.util.Log
 import com.photo.imagecompressor.tools.BuildConfig

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