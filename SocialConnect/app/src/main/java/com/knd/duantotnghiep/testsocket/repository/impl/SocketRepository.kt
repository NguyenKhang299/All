package com.knd.duantotnghiep.testsocket.repository.impl

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.knd.duantotnghiep.testsocket.response.MessageResponse
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject

@SuppressLint("CheckResult")
class SocketRepository @Inject constructor(private val mStompClient: StompClient) {
    init {
        mStompClient.lifecycle().subscribe {
            when (it.type) {
                LifecycleEvent.Type.OPENED -> {
                    // Xử lý khi kết nối mở
                    println("Connected to WebSocket")
                }

                LifecycleEvent.Type.ERROR -> {
                    // Xử lý khi có lỗi kết nối
                    println("Error in WebSocket connection: ${it.exception?.message}")
                }

                LifecycleEvent.Type.CLOSED -> {
                    // Xử lý khi kết nối đóng
                    println("Closed WebSocket connection")
                }

                else -> {}
            }
        }
    }

    fun sendMessage(messageResponse: MessageResponse) {
        mStompClient.topic("app/message").subscribe {
            it.payload
        }
    }

    fun sendMessage1(messageResponse: MessageResponse) {
        mStompClient.send("app/message/user/1",  Gson().toJson(messageResponse) )
    }
}