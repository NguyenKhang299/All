package com.knd.duantotnghiep.phonetrackerlocation.models

import java.io.Serializable

data class Notification(
    val _id_user_sender:String,
    var title: String,
    var body: String,
    val url :String?,
    val timeSender: Int,
    val type: String,
    val action: String
):Serializable{
    fun  setContentNotification(title: String, body: String) {
        this.title = title
        this.body = body
    }
}