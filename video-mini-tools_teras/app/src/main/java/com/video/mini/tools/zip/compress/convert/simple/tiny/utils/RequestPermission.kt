package com.video.mini.tools.zip.compress.convert.simple.tiny.utils

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData

class RequestPermission(context: FragmentActivity) : LiveData<Boolean>() {
    companion object {
        var permission = getImageAccessPermissions()
        private fun getImageAccessPermissions() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                arrayOf(
                    READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                )
            } else {
                arrayOf(READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
    }

    private val rqPermission =
        context.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            postValue(it)
        }

    private val rqPermissions =
        context.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            postValue(it.all { it.value })
        }

    fun launch(permission: String) = rqPermission.launch(permission)
    fun launch(permissions: Array<String>) = rqPermissions.launch(permissions)

}