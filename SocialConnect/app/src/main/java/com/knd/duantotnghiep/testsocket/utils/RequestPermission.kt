package com.knd.duantotnghiep.testsocket.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class PermissionStatus {
    data object Granted : PermissionStatus()
    data object Denied : PermissionStatus()
}

class RequestPermission(activity: FragmentActivity) {
    private val _status = MutableLiveData<PermissionStatus>()
    val status: LiveData<PermissionStatus> get() = _status

    private val requestPermission =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) _status.postValue(PermissionStatus.Granted) else _status.postValue(
                PermissionStatus.Denied
            )

        }
    private val requestMultiplePermissions =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it.all { it.value }) _status.postValue(PermissionStatus.Granted) else _status.postValue(
                PermissionStatus.Denied
            )

        }

    fun launch(permissions: String) = requestPermission.launch(permissions)
    fun launch(permissions: Array<String>) = requestMultiplePermissions.launch(permissions)
}

fun Activity.checkSelfPermission(permissions: Array<String>, status: (Boolean) -> Unit) {
    status(permissions.all { checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED })
}