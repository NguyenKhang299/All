package com.knd.duantotnghiep.phonetrackerlocation.models

import java.time.LocalDateTime

enum class FriendStatus {
    ACCEPT,
    REJECT,
    DELETE,
    SENT
}

data class Friends(
    val senderId: String,
    val receiverId: String,
    val status: FriendStatus = FriendStatus.SENT,
    val timeSend: Long
)
