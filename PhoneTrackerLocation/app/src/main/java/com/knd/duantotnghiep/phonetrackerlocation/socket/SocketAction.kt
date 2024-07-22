package com.knd.duantotnghiep.phonetrackerlocation.socket

import com.knd.duantotnghiep.phonetrackerlocation.models.BatteryInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatMessage
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo

sealed class SocketAction {
    class SendMessage(val chatMessage: ChatMessage) : SocketAction()
    object OnNewMessage : SocketAction()
    object OnlineStatusUpdates : SocketAction()
    object OfflineStatusUpdates : SocketAction()
    object LocationChanges : SocketAction()
    object ConnectSocket: SocketAction()
    object DisconnectSocket: SocketAction()
    data class SendLocation(val mapInfo: MapInfo, val batteryInfo: BatteryInfo) : SocketAction()
 }