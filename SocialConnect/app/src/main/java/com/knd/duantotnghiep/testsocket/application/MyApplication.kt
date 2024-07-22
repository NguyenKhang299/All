package com.knd.duantotnghiep.testsocket.application

import android.app.Application
import com.knd.duantotnghiep.testsocket.utils.MyNotificationManager
 import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication :Application() {
    override fun onCreate() {
        super.onCreate()
        MyNotificationManager.apply {
        createCallNotificationsChannelId(applicationContext)
            createMessageNotificationsChannelId(applicationContext)
        }
    }
}