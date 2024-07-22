package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.knd.duantotnghiep.phonetrackerlocation.utils.Permission.hasEnableGPS

object Permission {
    const val CAMERA = Manifest.permission.CAMERA
    const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    const val POST_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS
    const val AUDIO = Manifest.permission.RECORD_AUDIO
    fun Context.hasPermissionCamera(): Boolean {
        return checkSelfPermission(CAMERA) == PERMISSION_GRANTED
    }
    fun Context.hasPermissionRecordAudio(): Boolean {
        return checkSelfPermission(AUDIO) == PERMISSION_GRANTED
    }
    fun Context.hasPermissionPostNotification(): Boolean {
        return checkSelfPermission(POST_NOTIFICATION) == PERMISSION_GRANTED
    }

    @SuppressLint("WrongConstant")
    fun Context.hasPermissionLocation(): Boolean {
        return this.let {
            val checkFind =
                it.checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
            val checkCoarse =
                it.checkSelfPermission(ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
            checkFind || checkCoarse
        }
    }

    fun Context.hasEnableGPS(): Boolean {
        val locationMng = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationMng.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun Activity.turnOnGPS(callBack: (Boolean, IntentSenderRequest?) -> Unit) {
        val request = LocationRequest
            .Builder(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateDelayMillis(1000)
            .build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    val intentSender = it.resolution.intentSender
                    callBack(false, IntentSenderRequest.Builder(intentSender).build())
                } catch (_: IntentSender.SendIntentException) {
                    callBack(false, null)
                }
            }
        }.addOnSuccessListener {
            callBack(true, null)
        }
    }

    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)

        for (serviceInfo in runningServices) {
            if (serviceClass.name == serviceInfo.service.className) {
                Log.d("lllllllllll", "Running service1")
                return true
            }
        }
        Log.d("lllllllllll", "Running service")

        // Dịch vụ không được tìm thấy trong danh sách các dịch vụ đang chạy
        return false
    }
}