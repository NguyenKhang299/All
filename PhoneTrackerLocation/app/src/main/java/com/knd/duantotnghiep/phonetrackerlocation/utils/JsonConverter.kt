package com.knd.duantotnghiep.phonetrackerlocation.utils

import com.google.gson.Gson
import org.json.JSONObject

object JsonConverter {
    fun Any.convertToJsonObj(): JSONObject {
        val jsonStr = Gson().toJson(this)
        return JSONObject(jsonStr)
    }
}