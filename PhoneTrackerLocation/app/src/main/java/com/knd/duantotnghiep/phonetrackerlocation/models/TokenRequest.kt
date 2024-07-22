package com.knd.duantotnghiep.phonetrackerlocation.models

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("access_token") val token: String? =null,
    @SerializedName("idToken") val idToken: String? =null
)