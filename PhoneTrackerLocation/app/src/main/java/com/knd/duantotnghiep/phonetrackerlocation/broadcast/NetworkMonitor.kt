package com.knd.duantotnghiep.phonetrackerlocation.broadcast

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkMonitor @Inject constructor(@ApplicationContext context: Context) : LiveData<Boolean>() {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    override fun onActive() {//Được gọi khi số lượng người quan sát đang hoạt động thay đổi từ 0 thành 1.
        super.onActive()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onInactive() {//Called when the number of active observers change from 1 to 0.
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}