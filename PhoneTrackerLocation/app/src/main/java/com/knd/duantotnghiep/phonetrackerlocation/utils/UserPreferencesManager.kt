package com.knd.duantotnghiep.phonetrackerlocation.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.knd.duantotnghiep.phonetrackerlocation.models.UserRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPreferencesManager @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(Constants.USER_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    private val gson = Gson()
    fun saveUser(userRequest: UserRequest) {
        sharedPreferences.edit().apply {
            putString(Constants.CURRENT_USER, gson.toJson(userRequest))
        }.apply()
    }

    fun removeCurrentUser() = sharedPreferences.edit().apply {
        putString(Constants.CURRENT_USER, null)
    }.apply()

    fun getCurrentUser(): UserRequest? {
        return try {
            val jsonObj = sharedPreferences.getString(Constants.CURRENT_USER, null)
            gson.fromJson(jsonObj, UserRequest::class.java)
        } catch (e: Exception) {
            null
        }
    }
}