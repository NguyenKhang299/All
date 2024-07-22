package com.knd.duantotnghiep.phonetrackerlocation.respository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knd.duantotnghiep.phonetrackerlocation.models.BatteryInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatMessage
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatsResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.utils.Constants.BASE_URL
import com.knd.duantotnghiep.phonetrackerlocation.utils.JsonConverter.convertToJsonObj
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.TokenManager
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import javax.inject.Inject

class SocketRepository @Inject constructor(
    private val userPm: UserPreferencesManager,
    val socket: Socket
) {
    fun disconnect() {
        socket.disconnect()
    }

    fun sendLocation(mapInfo: MapInfo, batteryInfo: BatteryInfo) {
        socket.emit(
            "send-location-info",
            mapInfo.convertToJsonObj(),
            batteryInfo.convertToJsonObj()
        )
    }

    fun registerLocationChanges(listener: Emitter.Listener?) {
        socket.on("location", listener)
    }


    fun registerOnlineStatusListener(listener: Emitter.Listener?) {
        socket.on("receiver-onl", listener)
    }


    fun registerOfflineStatusListener(listener: Emitter.Listener?) {
        socket.on("receiver-off", listener)
    }

    fun registerNewMessageListener(listener: Emitter.Listener?) {
        socket.on("receiver-message", listener)
    }

    fun sendNewMessage(messageResponse: ChatMessage) {
        socket.emit("send-message", messageResponse.convertToJsonObj())
    }
}
