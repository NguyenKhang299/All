package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.app.admin.ConnectEvent
import io.socket.client.Socket

interface SocketCallBack {
    interface OnEventConnectionListener {
        fun onConnection()
        fun onDisconnect()
        fun onConnectionError()
    }
}

object SocketExtensions {
    fun Socket.onConnect(callBack: SocketCallBack.OnEventConnectionListener) {
       if (connected()) callBack.onConnection() else callBack.onDisconnect()
        on(Socket.EVENT_CONNECT) {
            callBack.onConnection()
            //   socket.emit("active-status", socket.id(), userPm.getCurrentUser()!!.convertToJsonObj())
        }
        on(Socket.EVENT_CONNECT_ERROR) {
            callBack.onConnectionError()
        }
        on(Socket.EVENT_DISCONNECT) {
            callBack.onDisconnect()
        }

    }
}