package com.knd.duantotnghiep.testsocket.core

import com.knd.duantotnghiep.testsocket.enum.StatusWithdraw
import com.knd.duantotnghiep.testsocket.enum.TypeChatMessageEnum
import java.util.Date

open class BaseChatDTO(
    val id: Int = -1,
    var senderID: Int = -1,
    var nickName: String = "",
    var avatar: String = "",
    var message: String? = null,
    var typeMessage: String = TypeChatMessageEnum.TEXT.name,
    var statusWithdraw: String = StatusWithdraw.NONE.name,
    var status: ArrayList<String> = ArrayList(),
     val createAt: Date = Date(System.currentTimeMillis())
) {

}
