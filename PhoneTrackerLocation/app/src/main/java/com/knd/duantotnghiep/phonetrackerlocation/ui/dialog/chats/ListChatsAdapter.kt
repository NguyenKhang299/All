package com.knd.duantotnghiep.phonetrackerlocation.ui.dialog.chats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseAdapter
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ItemChatBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatsResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.ContentItemChat
import com.knd.duantotnghiep.phonetrackerlocation.models.TypeMessage
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import java.text.SimpleDateFormat
import javax.inject.Inject

class ListChatsAdapter @Inject constructor() : BaseAdapter<ItemChatBinding, ChatsResponse>() {
    @Inject
    lateinit var userPm: UserPreferencesManager
    private lateinit var currentUser: UserRequest
    override fun getItemBinding(parent: ViewGroup): ItemChatBinding {
        currentUser = userPm.getCurrentUser()!!
        return ItemChatBinding.inflate(LayoutInflater.from(parent.context), null, false)
    }

    @SuppressLint("SimpleDateFormat")
     override fun bind(data: ChatsResponse, binding: ItemChatBinding) {
        val contentItemChat = getContentItem(data)
        binding.contentItemChat = contentItemChat
    }

    private fun getContentItem(data: ChatsResponse): ContentItemChat {
        val contentItemChat = ContentItemChat()
        if (data.messages.isNotEmpty()) {
            val lastMessage = data.messages.last()
            val isCurrentUserTheSender =  isCurrentUserSender(lastMessage.senderId)

            contentItemChat.messageLastSent = getLastMessageText(
                lastMessage.messageText,
                lastMessage.typeMessage,
                isCurrentUserTheSender
            )

            contentItemChat.nickName = if (isCurrentUserTheSender) lastMessage.receiverNickname else lastMessage.senderNickname
            contentItemChat.timeLastSent = lastMessage.formatSentTime()
        } else {
            contentItemChat.nickName = data.user.name
        }
        return contentItemChat
    }


    private fun getLastMessageText(
        message: String,
        typeMessage: TypeMessage,
        isCurrentUserSender: Boolean
    ): String {
        return when (typeMessage) {
            TypeMessage.FILE, TypeMessage.IMAGE, TypeMessage.VOICE -> {
                if (isCurrentUserSender) "Bạn đã gửi 1 tập tin"
                else "Bạn nhận được 1 tập tin"
            }

            else -> message
        }
    }



}