package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatsResponse
import com.knd.duantotnghiep.phonetrackerlocation.respository.ChatsRepository
import com.knd.duantotnghiep.phonetrackerlocation.respository.SocketRepository
import com.knd.duantotnghiep.phonetrackerlocation.socket.SocketViewModel
import com.knd.duantotnghiep.phonetrackerlocation.utils.NetworkResult
import com.knd.duantotnghiep.phonetrackerlocation.utils.handleApiCall
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListChatViewModel @Inject constructor(
    private val chatsRepository: ChatsRepository,
    private val socketRepository: SocketRepository
) : SocketViewModel(socketRepository) {
    //Livedata for observe get list chats users
    private val _onGetListChats = MutableLiveData<NetworkResult<List<ChatsResponse>>>()
    val onGetListChats: LiveData<NetworkResult<List<ChatsResponse>>> = _onGetListChats
    fun handle(action: ListChatAction) {
        when (action) {
            is ListChatAction.GetListChats -> getListChats()
        }
    }

    private fun getListChats() {
        handleApiCall(_onGetListChats, { chatsRepository.getChats() })
    }
}