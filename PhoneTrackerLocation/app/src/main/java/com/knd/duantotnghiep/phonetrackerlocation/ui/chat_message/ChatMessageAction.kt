package com.knd.duantotnghiep.phonetrackerlocation.ui.chat_message

sealed class ChatMessageAction {
    data class GetChatMessage(val id_other: String):ChatMessageAction()
}