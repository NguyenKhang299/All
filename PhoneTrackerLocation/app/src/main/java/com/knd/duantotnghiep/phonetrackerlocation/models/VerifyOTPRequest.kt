package com.knd.duantotnghiep.phonetrackerlocation.models

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("user")
    val userRequest: UserRequest,
    val otp: String,
 )