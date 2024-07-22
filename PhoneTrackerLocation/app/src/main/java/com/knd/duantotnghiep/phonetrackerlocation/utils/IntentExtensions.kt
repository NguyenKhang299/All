package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.content.Intent
import android.os.Build
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import java.io.Serializable

inline fun <reified T : Serializable> Intent.getSerializableExtra(key: String): T? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, T::class.java)
        } else {
            this.getSerializableExtra(key) as? T
        }
    }catch (e:Exception){
        null
    }
}