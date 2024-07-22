package com.knd.duantotnghiep.phonetrackerlocation.ui.chat_message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knd.duantotnghiep.phonetrackerlocation.broadcast.NetworkMonitor
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatMessage
import com.knd.duantotnghiep.phonetrackerlocation.respository.ChatsRepository
import com.knd.duantotnghiep.phonetrackerlocation.respository.SocketRepository
import com.knd.duantotnghiep.phonetrackerlocation.socket.SocketViewModel
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall
import javax.inject.Inject

class ChatMessageViewModel @Inject constructor(
    private val socketRepository: SocketRepository, private val chatsRepository: ChatsRepository,private val networkMonitor: NetworkMonitor
) :
    SocketViewModel(socketRepository) {
    //Live data for observer get chat messages in rooms
    private val _onGetChatMessage = MutableLiveData<NetworkResult<List<ChatMessage>>>()
    val onGetChatMessage: LiveData<NetworkResult<List<ChatMessage>>> = _onGetChatMessage

    //Livedata for observer Change Network
    val onChangeNetwork =networkMonitor
    fun handle(action: ChatMessageAction) {
        when (action) {
            is ChatMessageAction.GetChatMessage -> getChatMessage(action.id_other)
        }
    }

    private fun getChatMessage(id_other: String) {
        handleApiCall(_onGetChatMessage, { chatsRepository.getChatMessage(id_other) })
    }

}