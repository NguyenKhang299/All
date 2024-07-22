package com.knd.duantotnghiep.testsocket.response

import com.knd.duantotnghiep.testsocket.utils.FeedbackEnum
import com.knd.duantotnghiep.testsocket.utils.TypeNotificationEnum
import java.io.Serializable

data class NotificationResponse(
    val id: Int,
    val content: ChatResponse,
    val room: Room,
    val isGroup: Boolean = false,
    var type: String = TypeNotificationEnum.NONE.name,
    val feedback: String = FeedbackEnum.NONE.name
):Serializable

data class NotificationInfo(
    val id: Int,
    val name: String,
    val image: String,
    val isGroup: Boolean = false
)

data class Room(val id: Int, val name: String, val image: String) {

}

