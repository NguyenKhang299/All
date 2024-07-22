package com.knd.duantotnghiep.phonetrackerlocation.socket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.knd.duantotnghiep.phonetrackerlocation.models.BatteryInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatMessage
import com.knd.duantotnghiep.phonetrackerlocation.models.MapInfo
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.respository.SocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class SocketViewModel @Inject constructor(private val socketRepository: SocketRepository) :
    ViewModel() {
    // LiveData for observing socket connectivity status.
    private val _onConnectSocket by lazy { MutableLiveData<Boolean>() }
    val onConnectSocket: LiveData<Boolean> get() = _onConnectSocket

    // LiveData for observing user's friend online status.
    private val _onFriendsOnline by lazy { MutableLiveData<String>() }
    val onFriendsOnline: LiveData<String> get() = _onFriendsOnline

    // LiveData for observing user's friend online status.
    private val _onFriendsOffline by lazy { MutableLiveData<String>() }
    val onFriendsOffline: LiveData<String> get() = _onFriendsOffline

    // LiveData for observing changes in the location of the user's friends
    private val _onReceiverLocation by lazy { MutableLiveData<MapInfo>() }
    val onReceiverLocation: LiveData<MapInfo> get() = _onReceiverLocation

    // LiveData for observing new message
    private val _onReceiverNewMessage by lazy { MutableLiveData<ChatMessage>() }
    val onReceiverNewMessage: LiveData<ChatMessage> get() = _onReceiverNewMessage

    // LiveData for observing Send Message
    private val _onSendMessage by lazy { MutableLiveData<ChatMessage>() }
    val onSendMessage: LiveData<ChatMessage> get() = _onSendMessage

    init {
        handleSocket(SocketAction.OnlineStatusUpdates)
        handleSocket(SocketAction.OfflineStatusUpdates)
    }

    fun handleSocket(action: SocketAction) {
        when (action) {
            is SocketAction.OnlineStatusUpdates -> handleOnlineStatusUpdates()
            is SocketAction.OfflineStatusUpdates -> handleOfflineStatusUpdates()
            is SocketAction.LocationChanges -> handleLocationChanges()
            is SocketAction.ConnectSocket -> {}
            is SocketAction.DisconnectSocket -> socketRepository.disconnect()
            is SocketAction.SendLocation -> handleSendLocation(action.mapInfo, action.batteryInfo)
            is SocketAction.SendMessage -> handleSendMessage(action.chatMessage)
            is SocketAction.OnNewMessage -> handleOnNewMessage()
        }
    }

    private fun handleOnNewMessage() {
        val gson = Gson()
        socketRepository.registerNewMessageListener {
            _onReceiverNewMessage.postValue(
                gson.fromJson(
                    it[0].toString(),
                    ChatMessage::class.java
                )
            )
        }
    }

    private fun handleSendMessage(chatMessage: ChatMessage) {
        socketRepository.sendNewMessage(chatMessage)
    }


    private fun handleSendLocation(mapInfo: MapInfo, batteryInfo: BatteryInfo) =
        socketRepository.sendLocation(mapInfo, batteryInfo)

    private fun handleOnlineStatusUpdates() {
        socketRepository.registerOnlineStatusListener {
            _onFriendsOnline.postValue(it[0].toString())
        }
    }

    private fun handleOfflineStatusUpdates() {
        socketRepository.registerOfflineStatusListener {
            _onFriendsOffline.postValue(it[0].toString())
        }
    }

    private fun handleLocationChanges() {
        socketRepository.registerLocationChanges {
            val mapInfo = Gson().fromJson(it[0].toString(), MapInfo::class.java)
            _onReceiverLocation.postValue(mapInfo)
        }
    }
}