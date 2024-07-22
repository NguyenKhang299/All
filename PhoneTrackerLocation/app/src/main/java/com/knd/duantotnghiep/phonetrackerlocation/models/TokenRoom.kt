package com.knd.duantotnghiep.phonetrackerlocation.models

data class TokenRoom(
     var userId: String,
    var uid: String,
    var token: String,
    var expirationTimestamp: Long,
    var roomId: String
)

data class Room(
    var id: String? = null,
    var chanelId: String,
    var tokens: List<String> = ArrayList()
)