package com.knd.duantotnghiep.phonetrackerlocation.models

data class ContentItemChat(
    val _id: String? = null,
    var nickName: String = "",
    var avatar: String = "",
    var messageLastSent: String = "",
    var timeLastSent: String = "",
    var isSeen: Boolean = false
)