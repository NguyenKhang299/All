package com.knd.duantotnghiep.phonetrackerlocation.models

import com.knd.duantotnghiep.phonetrackerlocation.service.HandleSendMessage
import com.knd.duantotnghiep.phonetrackerlocation.utils.UserPreferencesManager
import java.text.SimpleDateFormat
import javax.inject.Inject

data class ChatMessage(
    val _id: String? = null,
    val senderId: String,
    val receiverId: String,
    val replyToMessageId: String? = null,
    val senderNickname: String,
    val receiverNickname: String,
    val messageText: String,
    val isSeen: Boolean = false,
    val feelings: List<Feeling> = emptyList(),
    val sentTime: Long = System.currentTimeMillis(),
    val seenTime: Long? = null,
    val typeMessage: TypeMessage = TypeMessage.TEXT
) {
    private val simpleDateFormat = SimpleDateFormat("HH:mm dd-MM")
    fun formatSentTime(): String = simpleDateFormat.format(sentTime)
    fun formatSeenTime(): String = simpleDateFormat.format(seenTime)
}


enum class TypeEmotion {
    HAPPY,//vui
    SAD,//buồn
    LOVE,//yêu
    ANGRY,//giận dữ
    SURPRISE,//ngạc nhiên
    LAUGH,//cười
    CRY,//khóc
    DISGUST//ghê tởm
}

enum class TypeMessage {
    TEXT, FILE, VOICE, IMAGE
}

data class Feeling(
    val _id: String,
    val _idChats: String,
    val _idUser: String,
    val type: TypeEmotion
)

data class ChatsResponse @Inject constructor(
    val _id: String,
    val user: UserRequest,
    var messages: List<ChatMessage>
) {

}