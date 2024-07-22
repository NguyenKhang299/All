package com.knd.duantotnghiep.phonetrackerlocation.remote

import com.knd.duantotnghiep.phonetrackerlocation.models.ChatMessage
import com.knd.duantotnghiep.phonetrackerlocation.models.ChatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatsApi {
    @GET("/chats")
    suspend fun getChats(): Response<List<ChatsResponse>>

    @GET("/chats/{_id_other}")
    suspend fun getChatMessage(@Path("_id_other") _id_other: String): Response<List<ChatMessage>>
}