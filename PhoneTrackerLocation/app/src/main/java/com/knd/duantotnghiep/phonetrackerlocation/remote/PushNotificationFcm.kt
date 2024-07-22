package com.knd.duantotnghiep.phonetrackerlocation.remote

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.knd.duantotnghiep.phonetrackerlocation.R
import com.knd.duantotnghiep.phonetrackerlocation.models.Notification
import com.knd.duantotnghiep.phonetrackerlocation.utils.JsonConverter.convertToJsonObj
import org.json.JSONException
import org.json.JSONObject

interface NotificationCallback {
    fun onSuccess()
    fun onError(error: String?)
}

object NotificationFcm {
    private var requestQueue: RequestQueue? = null
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    fun Send(
        context: Context,
        token: String,
        notification: Notification,
        callback: NotificationCallback
    ) {
        requestQueue = Volley.newRequestQueue(context)
        val body = JSONObject()
        body.put("to", token)
        body.put("data", notification.convertToJsonObj())
        try {
            val request: JsonObjectRequest = object : JsonObjectRequest(Method.POST, postUrl, body,
                { callback.onSuccess() },
                { callback.onError(it.localizedMessage) }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] = "key=${context.getString(R.string.fcmServerKey)}"
                    return header
                }
            }
            requestQueue!!.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

