package com.knd.duantotnghiep.phonetrackerlocation.respository

import com.knd.duantotnghiep.phonetrackerlocation.models.Notification
 import com.knd.duantotnghiep.phonetrackerlocation.remote.UserAPI
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {
    suspend fun getCurrentUser() = userAPI.getCurrentUser()
    suspend fun getInfoFriends() = userAPI.getInfoFriends()
    suspend fun updateProfile(name: String, address: String, multipartBody: MultipartBody.Part) =
        userAPI.updateProfile(name, address, multipartBody)

    suspend fun sendNotification(noti: Notification, id_receiver: String) = userAPI.sendNotification(noti, id_receiver)

     suspend fun getTokenRtc(_id_other: String) = userAPI.getTokenRtc(_id_other)
}