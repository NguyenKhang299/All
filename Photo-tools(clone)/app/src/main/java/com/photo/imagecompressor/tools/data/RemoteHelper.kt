package com.photo.imagecompressor.tools.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.photo.imagecompressor.tools.presentation.ShowFailedActivity

object RemoteHelper {
    @SuppressLint("StaticFieldLeak")
    var IS_FAILED: Boolean = false

    fun fetchAndActivate(context: Context) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
            setFetchTimeoutInSeconds(20)
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val showFailed = remoteConfig["show_failed"].asBoolean()
                    IS_FAILED = showFailed
                    if (showFailed) {
                        val intent = Intent(context, ShowFailedActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                }
            }
    }
}