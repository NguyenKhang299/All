package com.knd.duantotnghiep.phonetrackerlocation.broadcast

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knd.duantotnghiep.phonetrackerlocation.models.BatteryInfo
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BatteryMonitor @Inject constructor(@ApplicationContext private val context: Context) :
    BroadcastReceiver() {
    private val _batteryLiveData = MutableLiveData<BatteryInfo>()
    val batteryLiveData: LiveData<BatteryInfo> get() = _batteryLiveData

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        val batteryPct: Float = intent!!.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }
        val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
        val batteryInfo = BatteryInfo(batteryPct.toInt(), isCharging)
        Log.d("okokoko", "Battery"+batteryInfo)
        _batteryLiveData.postValue(batteryInfo)
    }

 
    fun registerReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        context.registerReceiver(this, intentFilter)
    }

    fun unregisterReceiver() {
        context.unregisterReceiver(this)
    }
}
