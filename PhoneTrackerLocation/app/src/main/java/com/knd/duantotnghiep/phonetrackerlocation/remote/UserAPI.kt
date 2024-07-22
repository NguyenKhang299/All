package com.knd.duantotnghiep.phonetrackerlocation.remote

import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.Notification
 import com.knd.duantotnghiep.phonetrackerlocation.models.RtcResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date
import javax.inject.Inject

interface UserAPI {
    @Multipart
    @PUT("users/update")
    suspend fun updateProfile(
        @Query("name") name: String,
        @Query("address") address: String,
        @Part avatar: MultipartBody.Part
    ): Response<MessageResponse>

    @GET("users/friendss")
    suspend fun getCurrentUser(): Response<UserRequest>

    @GET("users/friends")
    suspend fun getInfoFriends(): Response<List<UserRequest>>

    @PATCH("users/friends")
    suspend fun sendFriendRequest(@Body idFriend: String): Response<MessageResponse>

    @DELETE("users/friends")
    suspend fun deleteFriendRequest(@Body idFriend: String): Response<MessageResponse>

       @GET("users/rtctoken")
    suspend fun getTokenRtc(@Query("_id_other") _id_other: String): Response<RtcResponse>
    @POST("users/sendNoti")
    suspend fun sendNotification(
        @Body noti: Notification,
        @Query("id_receiver") id_receiver: String
    ): Response<MessageResponse>
}
