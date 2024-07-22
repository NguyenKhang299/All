package com.knd.duantotnghiep.phonetrackerlocation.ui.main

import android.view.View
import bolts.Capture
import com.knd.duantotnghiep.phonetrackerlocation.map.MapAction
import com.knd.duantotnghiep.phonetrackerlocation.models.BatteryInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed class MainViewAction {

    object TurnOnLocationSharing : MainViewAction()
    class StartLiveCapture(val viewCapture: View, val id: String, val oneShot: Boolean) :
        MainViewAction()
    class StopLiveCapture(val id: String) : MainViewAction()
    object TurnOffLocationSharing : MainViewAction()
    object GetCurrentUser : MainViewAction()
    object GetCurrentLocation : MainViewAction()
    object GetMapInfo : MainViewAction()
}
