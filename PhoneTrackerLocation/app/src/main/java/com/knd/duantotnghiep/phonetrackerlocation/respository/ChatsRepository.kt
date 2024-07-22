package com.knd.duantotnghiep.phonetrackerlocation.respository

import com.knd.duantotnghiep.phonetrackerlocation.models.ChatsResponse
import com.knd.duantotnghiep.phonetrackerlocation.remote.ChatsApi
import retrofit2.Response
import javax.inject.Inject

class ChatsRepository @Inject constructor(private val chatsApi: ChatsApi) {
    suspend fun getChats() = chatsApi.getChats()
    suspend fun getChatMessage(_id_other: String) = chatsApi.getChatMessage(_id_other)
}