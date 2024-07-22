package com.knd.duantotnghiep.phonetrackerlocation.models

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("user")
    val userRequest: UserRequest
){
}