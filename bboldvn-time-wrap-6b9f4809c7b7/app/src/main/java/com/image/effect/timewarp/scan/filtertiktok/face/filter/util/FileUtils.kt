package com.image.effect.timewarp.scan.filtertiktok.face.filter.util

import android.os.Environment
import android.util.Log
import java.io.File

class FileUtils {

    companion object {
        val TAG = "FileUtil"

        fun getExportDir(): File {
            val dir = File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Timewarp")
            Log.e(TAG, "getExportDir: dir: $dir")
            if(!dir.exists()) {
                dir.mkdirs()
            }

            return dir
        }
    }

}