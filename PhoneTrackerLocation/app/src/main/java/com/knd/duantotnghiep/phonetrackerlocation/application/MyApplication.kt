package com.knd.duantotnghiep.phonetrackerlocation.application

import android.app.Application
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.knd.duantotnghiep.phonetrackerlocation.notification.MyNotificationManager
import dagger.hilt.android.HiltAndroidApp

sealed class StatusApp {
    object Foreground : StatusApp()
    object Background : StatusApp()
}

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        lateinit var statusApp: StatusApp
    }
    private val defaultLifecycleObserver = object : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            statusApp=StatusApp.Foreground
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            statusApp=StatusApp.Background
        }
    }
    override fun onCreate() {
        super.onCreate()
        MyNotificationManager.createChanelId(applicationContext)
        ProcessLifecycleOwner.get().lifecycle.addObserver(defaultLifecycleObserver)
    }

//        if (!Permission.isServiceRunning(applicationContext, JobService::class.java)) {
//            val jobInfo = JobInfo.Builder(29, ComponentName(applicationContext, JobService::class.java))
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
//                    .setRequiresCharging(false)
//                    .setPersisted(true)
//                     .setMinimumLatency(500) // Độ trễ tối thiểu (ms)
//                    .setOverrideDeadline(500) // Hạn chót (ms)
//                    .build()
//            val jobScheduler = applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//            val resultCode = jobScheduler.schedule(jobInfo)
//        }

}