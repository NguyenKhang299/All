package com.photo.image.picture.tools.compressor.base

import android.app.Activity
import android.util.Log
import com.access.pro.config.ConfigModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.photo.image.picture.tools.compressor.BuildConfig


object RemoteConfigHelper {
    fun getConfigData() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                ConfigModel.timeInter = remoteConfig["time_inter"].asLong().toInt()
                BaseActivity.ENABLE_ADS = remoteConfig["enable_ads"].asBoolean()
                if (ConfigModel.forceUpdate && ConfigModel.forceUpdateVer > BuildConfig.VERSION_CODE) {

                }
            } else {
                // myApp.remoteDone = false
            }
        }
    }
}