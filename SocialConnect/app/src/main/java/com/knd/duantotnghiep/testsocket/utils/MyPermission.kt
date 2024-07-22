package com.knd.duantotnghiep.testsocket.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
const val POST_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS

fun Context.vibrator(milliseconds: Long) {
    val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vib.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vib.vibrate(milliseconds)
    }
}

fun Context.hasRecord(): Boolean {
    return checkSelfPermission(RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
}
fun Context.hasPostNoti(): Boolean {
    return checkSelfPermission(POST_NOTIFICATION) == PackageManager.PERMISSION_GRANTED
}