package com.knd.duantotnghiep.testsocket.my_interface

import com.knd.duantotnghiep.testsocket.response.ChatResponse

interface IImagesAdapter {
    interface ClickImageListener {
        fun onClickImage(chatResponse: ChatResponse)
    }
}