package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(Constants.PREFS_TOKEN_FILE, AppCompatActivity.MODE_PRIVATE)


    fun saveToken(token: String) {
        Log.d("dddddddddđ",token.toString())
        sharedPreferences.edit().apply {
            putString(Constants.USER_TOKEN, token)
        }.apply()
    }

    fun removeToken() = sharedPreferences.edit().apply {
        putString(Constants.USER_TOKEN, null)
    }.apply()

    fun getToken(): String? {
        return sharedPreferences.getString(Constants.USER_TOKEN, null)
    }
}