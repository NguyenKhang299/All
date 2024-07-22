package com.knd.duantotnghiep.phonetrackerlocation.remote

import com.knd.duantotnghiep.phonetrackerlocation.models.LoginRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.MessageResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.TokenRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import com.knd.duantotnghiep.phonetrackerlocation.models.UserResponse
import com.knd.duantotnghiep.phonetrackerlocation.models.VerifyOTPRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("auth/refresh-token")
    suspend fun refreshAccessToken(): String

    @POST("auth/signIn")
    suspend fun signIn(@Body loginRequest: LoginRequest): Response<UserResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/signUp")
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("auth/facebook")
    suspend fun authenticateFacebookToken(@Body tokenAccess: TokenRequest): Response<UserResponse>

    @POST("auth/google")
    suspend fun authenticateGoogleToken(@Header("idToken") idToken:String): Response<UserResponse>

    @POST("auth/send-otp")
    suspend fun sendOTP(@Query("phoneNumber") phoneNumber: String): Response<MessageResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOTPAndSignUp(
        @Body verifyOTPRequest:VerifyOTPRequest
    ): Response<MessageResponse>
}