package com.knd.duantotnghiep.phonetrackerlocation.models

import java.io.Serializable

data class UserRequest(
    val _id: String? = null,
    var name: String = "",
    var address: String = "",
    var avatar: String = "",
    var sex: Sex = Sex.FEMALE ,
    var authFaceBookId: String? = null,
    var authGoogleId: String? = null,
    var authType: AuthType = AuthType.LOCAL,
    var status: Boolean = true,
    var isShare: Boolean = false,
    var lastLongitude: Double = 0.0,
    var lastLatitude: Double = 0.0,
    var phoneNumber: String = "",
    var password: String = "",
) : Serializable {
}

enum class AuthType() {
    LOCAL,
    FACEBOOK,
    GOOGLE
}

enum class Sex {
    MALE,
    FEMALE,
}