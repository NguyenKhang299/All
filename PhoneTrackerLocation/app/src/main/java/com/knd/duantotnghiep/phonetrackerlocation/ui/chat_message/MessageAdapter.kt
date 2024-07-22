package com.knd.duantotnghiep.phonetrackerlocation.ui.chat_message

import android.view.LayoutInflater
import android.view.ViewGroup
import com.knd.duantotnghiep.phonetrackerlocation.core.BaseAdapter
import com.knd.duantotnghiep.phonetrackerlocation.databinding.ItemChatMessageBinding
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatMessage

class MessageAdapter : BaseAdapter<ItemChatMessageBinding, ChatMessage>() {
    override fun getItemBinding(parent: ViewGroup): ItemChatMessageBinding {
        return ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), null, false)

    }

    override fun bind(data: ChatMessage, binding: ItemChatMessageBinding) {
        binding.currentUser = isCurrentUserSender(data.senderId)
        binding.chatMessage = data
    }
}