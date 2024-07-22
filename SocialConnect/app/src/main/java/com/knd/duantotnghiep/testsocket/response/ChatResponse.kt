package com.knd.duantotnghiep.testsocket.response

import com.knd.duantotnghiep.testsocket.core.BaseChatDTO

class ChatResponse(
    var messageReply: ChatReplyResponse? = null,
    var sendTo: String="",
    var isSeen: Boolean = false,
    var files: ArrayList<UploadResponse> = ArrayList(),
    var feelings: ArrayList<String> = ArrayList(),
    var callDuration: Long = -1,
) : BaseChatDTO(){

}

